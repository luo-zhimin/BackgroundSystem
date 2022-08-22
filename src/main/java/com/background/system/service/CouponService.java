package com.background.system.service;

import com.background.system.entity.Coupon;
import com.background.system.response.CouponResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/20 16:12
 */
public interface CouponService {
    Page<CouponResponse> getCouponList(Integer page, Integer size);

    Coupon getCouponDetail(Long id);
}
