package com.background.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.background.system.entity.Caizhi;
import com.background.system.entity.Coupon;
import com.background.system.entity.Order;
import com.background.system.entity.Size;
import com.background.system.entity.token.Token;
import com.background.system.entity.vo.OrderVo;
import com.background.system.exception.ServiceException;
import com.background.system.mapper.*;
import com.background.system.response.OrderResponse;
import com.background.system.service.BaseService;
import com.background.system.service.OrderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
@Service
public class OrderServiceImpl extends BaseService implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private SizeMapper sizeMapper;

    @Resource
    private CaizhiMapper caizhiMapper;

    @Resource
    private CouponMapper couponMapper;

    @Autowired
    private PictureServiceImpl pictureService;

    @Autowired
    private AddressServiceImpl addressService;

    @Autowired
    private SizeServiceImpl sizeService;

    @Autowired
    private MaterialQualityServiceImpl materialQualityService;

    @Autowired
    private CouponServiceImpl couponService;

    @Override
    @Transactional
    public Long createOrder(OrderVo orderVo) {
        Order order = new Order();
        BeanUtil.copyProperties(orderVo, order);
        // 计算价格
        Size size = sizeMapper.selectById(order.getSizeId());
        Caizhi caizhi = caizhiMapper.selectById(order.getCaizhiId());
        if (size==null || caizhi==null){
            throw new ServiceException(1000,"请选择材质或者尺寸！");
        }
        BigDecimal uPrice = size.getUPrice();
        BigDecimal price = caizhi.getPrice();
        order.setTotal(uPrice.add(price));
        order.setIsPay(false);
        order.setIsDel(false);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        //谁下单的
        Token currentUser = getWeChatCurrentUser();
        order.setCreateUser(currentUser.getUsername());
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

    @Override
    public OrderResponse info(String orderId) {
        OrderResponse orderResponse = new OrderResponse();
        Order order = orderMapper.selectByPrimaryKey(orderId);
        BeanUtils.copyProperties(order,orderResponse);
        //图片 地址 尺寸 优惠卷
        if (CollectionUtils.isNotEmpty(order.getPictureIds())){
            orderResponse.setPictures(pictureService.getPicturesByIds(order.getPictureIds()));
        }
        if (order.getAddressId()!=null){
            orderResponse.setAddress(addressService.getAddressDetail(order.getAddressId()));
        }
        if (order.getSizeId()!=null){
            orderResponse.setSize(sizeService.getSizeDetail(order.getSizeId()+""));
        }
        if (order.getCaizhiId() != null) {
            orderResponse.setCaizhi(materialQualityService.getMaterialQualityDetail(order.getCaizhiId()));
        }
        if (order.getCouponId() != null) {
            orderResponse.setCoupon(couponService.getCouponDetail(order.getCouponId()));
        }
        return orderResponse;
    }

    @Override
    public Page<Order> getOrderList(Integer page, Integer size) {
        Page<Order> orderPage = initPage(page, size);
        Token currentUser = getWeChatCurrentUser();
        List<Order> orderList = orderMapper.getOrderList(page, size, currentUser.getUsername());
        int orderCount = orderMapper.getCurrentOrderCount(currentUser.getUsername());
        orderPage.setTotal(orderCount);
        orderPage.setRecords(orderList);
        return orderPage;
    }
}
