package com.background.system.mapper;

import com.background.system.entity.Coupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponMapper extends BaseMapper<Coupon> {

    List<Coupon> getCouponsList(@Param("page") Integer page,
                                @Param("size") Integer size,
                                @Param("openId")String openId);

    int countCouponsByCurrentUser(@Param("openId")String openId);
}