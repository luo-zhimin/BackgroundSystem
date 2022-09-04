package com.background.system.service;

import com.background.system.entity.Orderd;
import com.background.system.entity.vo.OrderVo;
import com.background.system.response.OrderdResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/8/23 09:47
 */
public interface OrderService {

    String createOrder(OrderVo orderVo);

    Boolean coupon(Long couponId, String orderId);

    Boolean changeAddress(String orderId, String addressId);

    OrderdResponse info(String orderId);

    Page<OrderdResponse> getOrderList(Integer page, Integer size);

    Boolean updateOrder(Orderd orderd);

    Boolean cancelOrder(String id);

    Page<OrderdResponse> getAdminOrderList(Integer page, Integer size, Integer type, String sizeId);

    Map<String,Integer> getAdminOrderCount();

    Boolean updateKdNo(String  id, String orderNo);

    Boolean orderClose(String  id);

    Boolean orderDownload(String id);

    Object getOrderCurrentDay();

//    Page<OrderResponse> getOrderAllList(Integer page, Integer size);
}
