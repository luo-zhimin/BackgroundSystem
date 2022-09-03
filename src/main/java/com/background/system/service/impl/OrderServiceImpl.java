package com.background.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.background.system.entity.*;
import com.background.system.entity.token.Token;
import com.background.system.entity.vo.OrderVo;
import com.background.system.exception.ServiceException;
import com.background.system.mapper.*;
import com.background.system.response.OrderCountResponse;
import com.background.system.response.OrderElementResponse;
import com.background.system.response.OrderResponse;
import com.background.system.response.file.ReadyDownloadFileResponse;
import com.background.system.service.OrderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        Size size = sizeMapper.selectByPrimaryKey(order.getSizeId()+"");
        Caizhi caizhi = caizhiMapper.selectByPrimaryKey(order.getCaizhiId());
        if (size==null || caizhi==null){
            throw new ServiceException(1000,"请选择材质或者尺寸！");
        }
        //尺寸价格
        BigDecimal uPrice = size.getUPrice();
        if (uPrice == null) {
            uPrice = size.getPrice();
        }
        //材质价格
//        BigDecimal price = caizhi.getPrice();
        //（材质+尺寸）
        BigDecimal total = uPrice;

        //价格计算 单 or 双 * 组  数量 *（材质+尺寸）
//        String faces = size.getFaces();
        //一共买了多少
        int totalNumber = orderElements.stream().mapToInt(OrderElement::getNumber).sum();
        total = total.multiply(BigDecimal.valueOf(totalNumber));

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
        if (CollectionUtils.isNotEmpty(orderElements)) {
            orderElements.forEach(orderElement -> {
                orderElement.setOrderId(order.getId());
                orderElement.setCreateTime(LocalDateTime.now());
            });
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
        Order order = orderMapper.selectByPrimaryKey(orderId);
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
    public Boolean changeAddress(String orderId, String addressId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
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
        if (StringUtils.isNotEmpty(order.getAddressId())){
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
        Page<OrderResponse> orderPage = initPage(page, size);
        Token currentUser = getWeChatCurrentUser();
        page = (page - 1) * size;
        List<Order> orderList = orderMapper.getOrderList(page, size, currentUser.getUsername());
        //商品
        List<OrderResponse> orderResponses =  transformOrderResponse(orderList);
        int orderCount = orderMapper.getCurrentOrderCount(currentUser.getUsername());
        orderPage.setTotal(orderCount);
        orderPage.setRecords(orderResponses);
        return orderPage;
    }

    @Override
    public Boolean updateOrder(Order order) {
        return orderMapper.updateByPrimaryKeySelective(order) > 0;
    }

    @Override
    @Transactional
    public Boolean cancelOrder(String id) {
        logger.info("cancelOrder [{}]",id);
        Token currentUser = getWeChatCurrentUser();
        Order order = orderMapper.selectByPrimaryKey(id);
        if (!order.getCreateUser().equals(currentUser.getUsername())){
            throw new ServiceException(1002,"请修改属于你自己的订单");
        }

        return orderMapper.deleteOrderById(id)>0;
    }

    @Override
    @SuppressWarnings({"all"})
    public Page<OrderResponse> getAdminOrderList(Integer page, Integer size,Integer type,String sizeId) {
        //todo 思路俩个接口 一个数量 一个列表 防止数量过大加载慢
        Page<OrderResponse> orderPage = initPage(page, size);
        if (type>=5){
            throw new ServiceException(1000,"类型暂无处理");
        }
        page = (page - 1) * size;
        //0-待付款  1-待发货 2-配送中 3-已完成 4-已取消
        List<String> sizeIds = Lists.newArrayList();
        if (StringUtils.isNotEmpty(sizeId)){
            sizeIds = Arrays.asList(sizeId.split(","));
        }

        int count = orderMapper.getOrderCountByType(type,sizeIds);
        List<Order>  orders = orderMapper.getOrderByType(page, size, type,sizeIds);
        List<OrderResponse> orderResponses =  transformOrderResponse(orders);
        orderPage.setTotal(count);
        orderPage.setRecords(orderResponses);
        return orderPage;
    }

    @Override
    public Map<String, Integer> getAdminOrderCount() {
        Map<String, Integer> countMap = initCountMap();
        //isPay 1 付款 待发货 有 kdNo 就是配送
        //待付款 待发货 配送中 已取消 已完成 支付金额
        //分组统计 最好设置 state 默认 为 0
        List<OrderCountResponse> orderCount = orderMapper.getOrderCount();
        //配送
        int orderNoCount = orderMapper.getHasKdNoCount();
        countMap.put("配送中",orderNoCount);
        int closeCount = orderMapper.getCloseCount();
        countMap.put("已完成",closeCount);
        if (CollectionUtils.isNotEmpty(orderCount)){
            List<OrderCountResponse> deleteResponse = orderCount.stream()
                    .filter(OrderCountResponse::getIsDel).collect(Collectors.toList());

            List<OrderCountResponse> noPay = orderCount.stream()
                    .filter(o -> !o.getIsPay()).collect(Collectors.toList());

            List<OrderCountResponse> pay = orderCount.stream()
                    .filter(o->o.getIsPay() && !o.getIsDel()).collect(Collectors.toList());

            orderCount.removeAll(deleteResponse);

            countMap.put("待付款",noPay.stream().mapToInt(OrderCountResponse::getPayCount).sum());
            countMap.put("待发货",pay.stream().mapToInt(OrderCountResponse::getPayCount).sum());
            countMap.put("已取消",deleteResponse.stream().mapToInt(OrderCountResponse::getDelCount).sum());

            countMap.put("支付金额",pay.stream().mapToInt(OrderCountResponse::getTotalCount).sum());
        }
        return countMap;
    }

    @Override
    @Transactional
    public Boolean updateKdNo(String  id, String orderNo) {
        logger.info("updateKdNo [{}] -- [{}]",id,orderNo);
        checkOrder(id);
        Token currentUser = getCurrentUser();
        return orderMapper.updateKdNo(id,orderNo,currentUser.getUsername())>0;
    }

    @Override
    @Transactional
    public Boolean orderClose(String  id) {
        logger.info("orderClose [{}]",id);
        checkOrder(id);
        Token currentUser = getWeChatCurrentUser();
        return orderMapper.closeOrder(id,currentUser.getUsername())>0;
    }

    @Override
    public Boolean orderDownload(String id) {
        logger.info("orderDownload[{}]",id);
        return orderMapper.orderDownload(id)>0;
    }

//    @Override
//    public Page<OrderResponse> getOrderAllList(Integer page, Integer size) {
//        List<OrderResponse> orderResponses = Lists.newArrayList();
//        Page<OrderResponse> orderPage = initPage(page, size);
//        List<Order> orderList = orderMapper.getOrderAllList(page, size);
//        //商品
//        if (CollectionUtils.isNotEmpty(orderList)){
//            orderList.forEach(order -> {
//                OrderResponse orderResponse = new OrderResponse();
//                BeanUtils.copyProperties(order,orderResponse);
//                if (order.getSizeId() != null) {
//                    orderResponse.setSize(sizeService.getSizeDetail(order.getSizeId()+""));
//                }
//                orderResponses.add(orderResponse);
//            });
//        }
//        int orderCount = orderList.size();
//        orderPage.setTotal(orderCount);
//        orderPage.setRecords(orderResponses);
//        return orderPage;
//    }

    private Map<String,Integer> initCountMap(){
        Map<String,Integer> countMap = new HashMap<>();
        countMap.put("待付款",0);
        countMap.put("待发货",0);
        countMap.put("配送中",0);
        countMap.put("已取消",0);
        countMap.put("已完成",0);
        countMap.put("支付金额",0);
        return countMap;
    }

    private List<OrderResponse> transformOrderResponse(List<Order> orderList){
        List<OrderResponse> orderResponses = Lists.newArrayList();
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
        return orderResponses;
    }

    private void checkOrder(String id){
        if (StringUtils.isEmpty(id)){
            throw new ServiceException(1003,"id不可以为空");
        }
        Order live = orderMapper.selectByPrimaryKey(id);
        if (live==null){
            throw new ServiceException(1004,"该订单不存在，请确认后重新操作");
        }
    }


    /**
     * 得到没有zip链接的
     * @return 所有待处理对象
     */
    public List<ReadyDownloadFileResponse> getFile(){
        //找到支付 没有删除的订单 并且没有链接生产的
        List<ReadyDownloadFileResponse> orders = orderMapper.getNoZipPathOrder();
        if (CollectionUtils.isEmpty(orders)){
            return Collections.emptyList();
        }
        orders.forEach(order->{
            order.setPictures(pictureService.getPicturesByIds(order.getPictureIds()));
        });
        return orders;
    }
}
