package com.background.system.service.impl;

import com.background.system.entity.Coupon;
import com.background.system.entity.Picture;
import com.background.system.entity.token.Token;
import com.background.system.mapper.CouponMapper;
import com.background.system.response.CouponResponse;
import com.background.system.service.CouponService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public Page<CouponResponse> getCouponList(Integer page, Integer size) {
        Page<CouponResponse> couponPage = initPage(page, size);
        Token weChatCurrentUser = getWeChatCurrentUser();
        List<CouponResponse> responses = Lists.newArrayList();
        List<Coupon> coupons = couponMapper.getCouponsList((page - 1) * size, size,
                weChatCurrentUser.getUsername());
        if (CollectionUtils.isEmpty(coupons)) {
            return couponPage;
        }
        List<String> pictureIds = coupons.stream().map(Coupon::getPictureId).collect(Collectors.toList());
        List<Picture> pictures = pictureService.getPicturesByIds(pictureIds);
        coupons.forEach(coupon -> {
            CouponResponse response = new CouponResponse();
            BeanUtils.copyProperties(coupon,response);
            response.setPicture(pictures.stream().filter(picture ->
                    coupon.getPictureId().equals(picture.getId())).findFirst().orElse(new Picture()));
            responses.add(response);
        });
        Long qualities = couponMapper.selectCount(
                new QueryWrapper<Coupon>()
                        .eq("open_id", weChatCurrentUser.getUsername()));
        couponPage.setTotal(qualities);
        couponPage.setRecords(responses);
        return couponPage;
    }

    @Override
    public Coupon getCouponDetail(Long id) {
        return couponMapper.selectById(id);
    }
}
