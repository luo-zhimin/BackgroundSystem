package com.background.system.service;

import com.background.system.entity.Order;
import com.background.system.entity.vo.OrderVo;
import com.background.system.response.OrderResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

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

    Page<OrderResponse> getOrderList(Integer page, Integer size);

    Boolean updateOrder(Order order);

    Boolean cancelOrder(String id);

    Page<OrderResponse> getAdminOrderList(Integer page, Integer size,String type);

    Map<String,Integer> getAdminOrderCount();
}
