package com.background.system.service.impl;

import com.background.system.entity.Coupon;
import com.background.system.entity.token.Token;
import com.background.system.mapper.CouponMapper;
import com.background.system.service.BaseService;
import com.background.system.service.CouponService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Page<Coupon> getCouponList(Integer page, Integer size) {
        Page<Coupon> couponPage = initPage(page, size);
        Token weChatCurrentUser = getWeChatCurrentUser();
        List<Coupon> coupons = couponMapper.getCouponsList((page - 1) * size, size,
                weChatCurrentUser.getUsername());
        if (CollectionUtils.isEmpty(coupons)){
            return couponPage;
        }
        Long qualities = couponMapper.selectCount(
                new QueryWrapper<Coupon>()
                        .eq("open_id", weChatCurrentUser.getUsername()));
        couponPage.setTotal(qualities);
        couponPage.setRecords(coupons);
        return couponPage;
    }

    @Override
    public Coupon getCouponDetail(Long id) {
        return couponMapper.selectById(id);
    }
}
