package com.background.system.service;

import com.background.system.entity.vo.OrderVo;
import com.background.system.response.OrderResponse;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/8/23 09:47
 */
public interface OrderService {

    Long createOrder(OrderVo orderVo);

    Boolean coupon(Long couponId, String orderId);

    Boolean changeAddress(String orderId, Long addressId);

    OrderResponse info(String orderId);
}
