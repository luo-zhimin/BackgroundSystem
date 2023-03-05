package com.background.system.service.impl;

import com.background.system.entity.Coupon;
import com.background.system.entity.Picture;
import com.background.system.entity.token.Token;
import com.background.system.exception.ServiceException;
import com.background.system.mapper.CouponMapper;
import com.background.system.request.BaseRequest;
import com.background.system.response.CouponResponse;
import com.background.system.service.CouponService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/20 16:12
 */
@Service
@Slf4j
public class CouponServiceImpl extends BaseService implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private PictureServiceImpl pictureService;

    private final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Override
    public Page<CouponResponse> getCouponList(Integer page, Integer size,String type,Boolean isUse,Boolean status) {
        Page<CouponResponse> couponPage = initPage(page, size);
        Token weChatCurrentUser = null;
        switch (type){
            case "wechat":
                weChatCurrentUser = getWeChatCurrentUser();
                break;
            case "admin":
                //逻辑待处理
                break;
            default:
                break;
        }
        List<CouponResponse> responses = Lists.newArrayList();
        List<Coupon> coupons = couponMapper.getCouponsList((page - 1) * size, size,
                weChatCurrentUser!=null ? weChatCurrentUser.getUsername() : "",isUse,status);
        if (CollectionUtils.isEmpty(coupons)) {
            return couponPage;
        }
        List<String> pictureIds = coupons.stream().map(Coupon::getPictureId).filter(Objects::nonNull).collect(Collectors.toList());
        List<Picture> pictures = pictureService.getPicturesByIds(pictureIds);
        coupons.forEach(coupon -> {
            CouponResponse response = new CouponResponse();
            BeanUtils.copyProperties(coupon,response);
            response.setPicture(StringUtils.isNotEmpty(coupon.getPictureId())? pictures.stream().filter(picture ->
                    coupon.getPictureId().equals(picture.getId())).findFirst().orElse(new Picture()) : new Picture());
            responses.add(response);
        });
        int qualities = couponMapper.selectCountByOpenId(weChatCurrentUser!=null ? weChatCurrentUser.getUsername() : "");
        couponPage.setTotal(qualities);
        couponPage.setRecords(responses);
        return couponPage;
    }

    @Override
    @Cacheable(value = "coupon",key = "#id")
    public Coupon getCouponDetail(Long id) {
        return couponMapper.selectById(id);
    }

    @Override
    @Transactional
    @CachePut(value = "coupon",key = "#request.couponId")
    public Boolean covertCoupon(BaseRequest request) {
        log.info("coverCoupon request[{}]",request);
        if (StringUtils.isEmpty(request.getCouponId())){
            throw new ServiceException(1003,"兑换码不可以为空");
        }

        Coupon coupon = couponMapper.selectOne(new QueryWrapper<Coupon>()
                .eq("coupon_id", request.getCouponId()));

        if (coupon==null){
            throw new ServiceException(1004,"该兑换码不存在，请确认后在操作");
        }

        if (StringUtils.isNotEmpty(coupon.getOpenId()) || coupon.getIsUsed()){
            throw new ServiceException(1004,"该兑换码已经使用，请确认后在操作");
        }

        if (coupon.getStatus()){
            throw new ServiceException(1004,"该兑换码已经过期，请确认后在操作");
        }

        Token currentUser = getWeChatCurrentUser();
        return couponMapper.updateCouponUserById(currentUser.getUsername(), request.getCouponId())>0;
    }

    @Override
    @Transactional
    @CachePut(value = "coupon",key = "#coupon.id")
    public Boolean insert(Coupon coupon) {
        log.info("coupon insert coupon[{}]",coupon);
        //随机生成优惠卷兑换码
        String uuid = UUID.randomUUID().toString().replace("-", "");
        coupon.setCouponId(uuid);
        if (coupon.getPrice()==null && coupon.getUseLimit()==null){
            throw new ServiceException(1006,"价格和消费限制不可以为空");
        }
        if(coupon.getUseLimit()<=0){
            throw new ServiceException(1007,"优惠卷消费限制必须大于0");
        }
        if (coupon.getPrice()!=null && coupon.getPrice().compareTo(BigDecimal.valueOf(coupon.getUseLimit()))>=0){
            //1001 1000
            throw new ServiceException(1008,"优惠卷消费价格需要小于消费限制");
        }
        return couponMapper.insertSelective(coupon)>0;
    }

    @Override
    @Transactional
    @CachePut(value = "coupon",key = "#coupon.id")
    public Boolean update(Coupon coupon) {
        log.info("coupon update coupon[{}]",coupon);
        if (coupon.getId()==null){
            throw new ServiceException(1004,"id不可以为空");
        }
        Coupon liveCoupon = couponMapper.selectByPrimaryKey(coupon.getId());
        if (liveCoupon==null){
            throw new ServiceException(1005,"该优惠卷不存在");
        }
        if(coupon.getUseLimit()!=null && coupon.getUseLimit()<=0){
            throw new ServiceException(1007,"优惠卷消费限制必须大于0");
        }
        return couponMapper.updateByPrimaryKeySelective(coupon)>0;
    }

    @Override
    @Cacheable(value = "coupon")
    public List<Coupon> selectCoupons(Coupon coupon) {
        return couponMapper.selectList(new QueryWrapper<>(coupon));
    }

    @Transactional(rollbackFor = ServiceException.class)
    public void closeCoupon(){
        List<Long> couponIds = this.couponMapper.getCloseCoupon();
        if (CollectionUtils.isEmpty(couponIds)){
            logger.info("no coupon need handle");
            return;
        }
        this.couponMapper.close(couponIds);
    }

    @Transactional
    @CachePut(value = "coupon",key = "#coupon.id")
    public Boolean updateService(Coupon coupon) {
        return couponMapper.updateByPrimaryKeySelective(coupon)>0;
    }
}
