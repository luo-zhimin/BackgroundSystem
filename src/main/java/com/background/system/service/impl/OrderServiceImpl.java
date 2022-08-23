package com.background.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.background.system.entity.Caizhi;
import com.background.system.entity.Coupon;
import com.background.system.entity.Order;
import com.background.system.entity.Size;
import com.background.system.entity.vo.OrderVo;
import com.background.system.mapper.*;
import com.background.system.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private SizeMapper sizeMapper;

    @Resource
    private CaizhiMapper caizhiMapper;

    @Resource
    private CouponMapper couponMapper;

    @Override
    @Transactional
    public Long createOrder(OrderVo orderVo) {
        Order order = new Order();
        BeanUtil.copyProperties(orderVo, order);
        // 计算价格
        Size size = sizeMapper.selectById(order.getSizeId());
        Caizhi caizhi = caizhiMapper.selectById(order.getCaizhiId());
        BigDecimal uPrice = size.getUPrice();
        BigDecimal price = caizhi.getPrice();
        order.setTotal(uPrice.add(price));
        orderMapper.insert(order);
        return order.getId();
    }

    @Override
    @Transactional
    public BigDecimal coupon(Long couponId, String orderId) {
        Coupon coupon = couponMapper.selectById(couponId);
        BigDecimal price = coupon.getPrice();
        Order order = orderMapper.selectById(orderId);
        BigDecimal total = order.getTotal();
        BigDecimal newTotal = total.subtract(price);
        if (newTotal.compareTo(BigDecimal.ZERO) <= 0) {
            newTotal = BigDecimal.ZERO;
        }
        order.setTotal(newTotal);
        order.setCouponId(couponId);
        orderMapper.updateById(order);
        //如果要是要需要更新优惠卷使用
        couponMapper.updateIsUsedCoupon(couponId);
        return newTotal;
    }

    @Override
    @Transactional
    public Boolean changeAddress(String orderId, Long addressId) {
        Order order = orderMapper.selectById(orderId);
        order.setAddressId(addressId);
        return orderMapper.updateById(order)>0;
    }
}
