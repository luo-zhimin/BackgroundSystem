package com.background.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.background.system.entity.*;
import com.background.system.entity.token.Token;
import com.background.system.entity.vo.OrderVo;
import com.background.system.exception.ServiceException;
import com.background.system.mapper.CaizhiMapper;
import com.background.system.mapper.CouponMapper;
import com.background.system.mapper.OrderElementsMapper;
import com.background.system.mapper.OrderMapper;
import com.background.system.response.CountResponse;
import com.background.system.response.OrderCountResponse;
import com.background.system.response.OrderElementResponse;
import com.background.system.response.OrderdResponse;
import com.background.system.response.file.ReadyDownloadFileResponse;
import com.background.system.service.OrderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
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
import java.time.format.DateTimeFormatter;
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
    public String createOrder(OrderVo orderVo) {
        Orderd order = new Orderd();

        if (StringUtils.isEmpty(orderVo.getCaizhiId())){
            throw new ServiceException(1003,"请选择材质后在进行操作");
        }

        BeanUtil.copyProperties(orderVo, order);

        Caizhi caizhi = caizhiMapper.selectById(order.getCaizhiId());
        List<OrderElement> orderElements = orderVo.getOrderElements();
        //多个
//        //材质价格
//        //（材质+尺寸）
        BigDecimal total = caizhi.getPrice();
//
//        //价格计算 单 or 双 * 组  数量 *（材质+尺寸）
//        //一共买了多少
        int totalNumber = orderElements.stream().mapToInt(OrderElement::getNumber).sum();
        total = total.multiply(BigDecimal.valueOf(totalNumber));

        order.setTotal(total);
//        order.setIsPay(false);
//        order.setIsDel(false);
//        order.setStatus("0");
//        order.setCreateTime(LocalDateTime.now());
//        order.setUpdateTime(LocalDateTime.now());
        //谁下单的
        Token currentUser = getWeChatCurrentUser();
        order.setCreateUser(currentUser.getUsername());
        orderMapper.insertSelective(order);
        //下单时候 具体详情进入 element里面
        logger.info("order elements[{}]", JSON.toJSONString(orderElements));
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
        Orderd order = orderMapper.selectByPrimaryKey(orderId);
        order.setStatus("1");
        order.setIsPay(true);
        order.setCouponId(couponId);
        orderMapper.updateByPrimaryKeySelective(order);

        // 优惠券标记使用过
        coupon.setIsUsed(true);
        Token weChatCurrentUser = getWeChatCurrentUser();
        String username = weChatCurrentUser.getUsername();
        coupon.setOpenId(username);
        couponMapper.updateByPrimaryKeySelective(coupon);

        return true;
    }

    @Override
    @Transactional
    public Boolean changeAddress(String orderId, String addressId) {
        Orderd order = orderMapper.selectByPrimaryKey(orderId);
        order.setAddressId(addressId);
        return orderMapper.updateById(order)>0;
    }

    @Override
    public OrderdResponse info(String orderId) {
        OrderdResponse orderResponse = new OrderdResponse();
        Orderd order = orderMapper.selectByPrimaryKey(orderId);
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

        if (StringUtils.isNotEmpty(order.getSizeId())){
            orderResponse.setSize(sizeService.getSizeDetail(order.getSizeId()));
        }

        orderResponse.setMaterialQualities(materialQualityService.getMaterialListByIds(order.getMaterialQualityIds()));

        if (order.getCouponId() != null) {
            orderResponse.setCoupon(couponService.getCouponDetail(order.getCouponId()));
        }
        orderResponse.setNum(elements.stream().mapToInt(OrderElement::getNumber).sum());
        return orderResponse;
    }

    @Override
    public Page<OrderdResponse> getOrderList(Integer page, Integer size) {
        Page<OrderdResponse> orderPage = initPage(page, size);
        Token currentUser = getWeChatCurrentUser();
        page = (page - 1) * size;
        List<Orderd> orderList = orderMapper.getOrderList(page, size, currentUser.getUsername());
        //商品
        List<OrderdResponse> orderResponses =  transformOrderResponse(orderList);
        int orderCount = orderMapper.getCurrentOrderCount(currentUser.getUsername());
        orderPage.setTotal(orderCount);
        orderPage.setRecords(orderResponses);
        return orderPage;
    }

    @Override
    @Transactional
    public Boolean updateOrder(Orderd order) {
        //判断优惠卷限制
        if (order.getCouponId()!=null && order.getCouponId()!=0){
            Coupon coupon = couponService.getCouponDetail(order.getCouponId());
            Integer userLimit = coupon.getUseLimit();
            if (order.getTotal()!=null && order.getTotal().compareTo(BigDecimal.valueOf(userLimit))<0){
                throw new ServiceException(1004,"您不符合当前优惠卷限制，请重新选择");
            }
        }
        return orderMapper.updateByPrimaryKeySelective(order) > 0;
    }

    @Override
    @Transactional
    public Boolean cancelOrder(String id) {
        logger.info("cancelOrder [{}]",id);
        Token currentUser = getWeChatCurrentUser();
        Orderd order = orderMapper.selectByPrimaryKey(id);
        if (!order.getCreateUser().equals(currentUser.getUsername())){
            throw new ServiceException(1002,"请修改属于你自己的订单");
        }
        //0-待付款 1-待发货 2-配送中 3-已完成 4-已取消
        return orderMapper.deleteOrderById(id)>0;
    }

    @Override
    @SuppressWarnings({"all"})
    public Page<OrderdResponse> getAdminOrderList(Integer page, Integer size,Integer type,String sizeId) {
        //todo 思路俩个接口 一个数量 一个列表 防止数量过大加载慢
        Page<OrderdResponse> orderPage = initPage(page, size);
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
        List<Orderd>  orders = orderMapper.getOrderByType(page, size, type,sizeIds);
        List<OrderdResponse> orderResponses =  transformOrderResponse(orders);
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
        //todo 可以适当改造 前面维护status  0-待付款  1-待发货 2-配送中 3-已完成 4-已取消
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
        //0-待付款  1-待发货 2-配送中 3-已完成 4-已取消
        return orderMapper.closeOrder(id,currentUser.getUsername())>0;
    }

    @Override
    public Boolean orderDownload(String id) {
        logger.info("orderDownload[{}]",id);
        return orderMapper.orderDownload(id)>0;
    }

    @Override
    public Object getOrderCurrentDay() {
        String currentDay = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
        return orderMapper.getIndexOrderCount(currentDay);
    }

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

    private List<OrderdResponse> transformOrderResponse(List<Orderd> orderList){
        List<OrderdResponse> orderResponses = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(orderList)){
            //数量转换
            List<CountResponse> countResponses = elementsMapper.
                    getOrderCountByIds(orderList.stream().map(Orderd::getId).collect(Collectors.toList()));

            Map<String, Integer> countMap = countResponses.stream()
                    .collect(Collectors.toMap(CountResponse::getOrderId, CountResponse::getNumber));

            orderList.forEach(order -> {
                OrderdResponse orderResponse = new OrderdResponse();
                BeanUtils.copyProperties(order,orderResponse);
                if (StringUtils.isNotEmpty(order.getSizeId())) {
                    orderResponse.setSize(sizeService.getSizeDetail(order.getSizeId()));
                }

                orderResponse.setNum(countMap.isEmpty() ? 0 : Optional.ofNullable(countMap.get(order.getId())).orElse(0));
                orderResponses.add(orderResponse);
            });
        }
        return orderResponses;
    }

    private void checkOrder(String id){
        if (StringUtils.isEmpty(id)){
            throw new ServiceException(1003,"id不可以为空");
        }
        Orderd live = orderMapper.selectByPrimaryKey(id);
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

        List<SourceOrderPicture> sourceOrderPictures = Lists.newArrayList();

        orders.forEach(order -> {
            sourceOrderPictures.add(SourceOrderPicture.builder().orderId(order.getId()).pictureIds(order.getPictureIds()).build());
//            order.setPictures(pictureService.getPicturesByIds(order.getPictureIds()));
            order.setPictureMap(pictureService.getPicturesByIds(order.getPictureIds()).stream().collect(Collectors.groupingBy(Picture::getId)));
        });
        //18, 19, 23, 30, 29, 22, 20, 17, 21, 24, 31, 32, 25, 33, 34, 36, 35, 27
        logger.info("source [{}]",sourceOrderPictures);
        //二次处理 sort
        orders.forEach(order->{
            sourceOrderPictures.forEach(source->{
                if (order.getId().equals(source.getOrderId())){
                    source.getPictureIds().forEach(pId->{
                        order.getPictures().addAll(Optional.ofNullable(order.getPictureMap().get(pId)).orElse(Collections.emptyList()));
                    });
                }
            });
        });
        return orders;
    }


    @Data
    @ToString
    @Builder
    static class SourceOrderPicture{

        private String orderId;

        private List<String> pictureIds;
    }
}
