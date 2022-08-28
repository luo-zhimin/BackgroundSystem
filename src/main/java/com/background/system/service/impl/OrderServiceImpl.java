package com.background.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.background.system.entity.*;
import com.background.system.entity.token.Token;
import com.background.system.entity.vo.OrderVo;
import com.background.system.exception.ServiceException;
import com.background.system.mapper.*;
import com.background.system.response.OrderElementResponse;
import com.background.system.response.OrderResponse;
import com.background.system.service.OrderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

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

    @Autowired
    private OrderElementsMapper elementsMapper;

    @Override
    @Transactional
    public Long createOrder(OrderVo orderVo) {
        Order order = new Order();
        BeanUtil.copyProperties(orderVo, order);
        List<OrderElement> orderElements = orderVo.getOrderElements();
        // 计算价格
        Size size = sizeMapper.selectById(order.getSizeId());
        Caizhi caizhi = caizhiMapper.selectById(order.getCaizhiId());
        if (size==null || caizhi==null){
            throw new ServiceException(1000,"请选择材质或者尺寸！");
        }
        //尺寸价格
        BigDecimal uPrice = size.getUPrice();
        //材质价格
        BigDecimal price = caizhi.getPrice();
        //（材质+尺寸）
        BigDecimal total = uPrice.add(price);

        //价格计算 单 or 双 * 组  数量 *（材质+尺寸）
//        String faces = size.getFaces();
        //一共买了多少
        int totalNumber = orderElements.stream().mapToInt(OrderElement::getNumber).sum();
        total = total.multiply(BigDecimal.valueOf(totalNumber));

//        if (faces.equals("单面")) {
//        } else if (faces.equals("双面")) {
//            total = total.multiply(BigDecimal.valueOf((order.getPictureIds().size() / 2)));
//        }

        //运费
        BigDecimal portPrice = order.getPortPrice();
        if (portPrice!=null){
            total = total.add(portPrice);
        }
        order.setTotal(total);
        order.setIsPay(false);
        order.setIsDel(false);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        //谁下单的
        Token currentUser = getWeChatCurrentUser();
        order.setCreateUser(currentUser.getUsername());
        orderMapper.insert(order);
        //下单时候 具体详情进入 element里面
        logger.info("order elements[{}]", orderElements);
        if (CollectionUtils.isNotEmpty(orderElements)){
            orderElements.forEach(orderElement -> orderElement.setOrderId(order.getId()));
            elementsMapper.batchInsert(orderElements);
        }
        return order.getId();
    }

    @Override
    @Transactional
    // 只有在支付成功时才会调用，直接干掉优惠券
    public Boolean coupon(Long couponId, String orderId) {
        Coupon coupon = couponMapper.selectById(couponId);

        if (coupon.getIsUsed()) {
            return false;
        }

        // 更新订单
        Order order = orderMapper.selectById(orderId);
        order.setStatus("1");
        order.setIsPay(true);
        order.setCouponId(couponId);
        orderMapper.updateById(order);

        // 优惠券标记使用过
        coupon.setIsUsed(true);
        Token weChatCurrentUser = getWeChatCurrentUser();
        String username = weChatCurrentUser.getUsername();
        coupon.setOpenId(username);
        couponMapper.updateById(coupon);

        return true;
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
        //elements
        List<OrderElement> elements = elementsMapper.getOrderElementsByOrderId(orderId);
        if (CollectionUtils.isNotEmpty(elements)){
            List<OrderElementResponse> elementResponses = Lists.newArrayList();
            elements.forEach(orderElement -> {
                OrderElementResponse elementResponse = new OrderElementResponse();
                BeanUtils.copyProperties(orderElement,elementResponse);
                elementResponse.setPictures(pictureService.getPicturesByIds(orderElement.getPictureIds()));
                elementResponses.add(elementResponse);
            });
            orderResponse.setElements(elementResponses);
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
        orderResponse.setNum(elements.stream().mapToInt(OrderElement::getNumber).sum());
        return orderResponse;
    }

    @Override
    public Page<OrderResponse> getOrderList(Integer page, Integer size) {
        List<OrderResponse> orderResponses = Lists.newArrayList();
        Page<OrderResponse> orderPage = initPage(page, size);
        Token currentUser = getWeChatCurrentUser();
        List<Order> orderList = orderMapper.getOrderList(page, size, currentUser.getUsername());
        //商品
        if (CollectionUtils.isNotEmpty(orderList)){
            orderList.forEach(order -> {
                OrderResponse orderResponse = new OrderResponse();
                BeanUtils.copyProperties(order,orderResponse);
                if (order.getSizeId() != null) {
                    orderResponse.setSize(sizeService.getSizeDetail(order.getSizeId()+""));
                }
                orderResponses.add(orderResponse);
            });
        }
        int orderCount = orderMapper.getCurrentOrderCount(currentUser.getUsername());
        orderPage.setTotal(orderCount);
        orderPage.setRecords(orderResponses);
        return orderPage;
    }

    @Override
    public Boolean updateOrder(Order order) {
        return orderMapper.updateByPrimaryKeySelective(order) > 0;
    }
}
