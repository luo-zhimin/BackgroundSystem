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
                                @Param("openId") String openId,
                                @Param("isUse") Boolean isUse,
                                @Param("status") Boolean status);

    int countCouponsByCurrentUser(@Param("openId") String openId);

    int updateIsUsedCoupon(@Param("couponId") Long couponId);

    Boolean getCouponByCouponId(@Param("couponId") String couponId);

    int updateCouponUserById(@Param("openId") String openId, @Param("id") String id);

    int insertSelective(Coupon coupon);

    Coupon selectByPrimaryKey(@Param("id") Long id);

    int updateByPrimaryKeySelective(Coupon coupon);

    int selectCountByOpenId(@Param("openId") String openId);

    List<Long> getCloseCoupon();

    int close(@Param("ids")List<Long> ids);
}