package com.background.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.background.system.entity.*;
import com.background.system.entity.token.Token;
import com.background.system.entity.vo.OrderVo;
import com.background.system.exception.ServiceException;
import com.background.system.mapper.OrderElementsMapper;
import com.background.system.mapper.OrderMapper;
import com.background.system.response.CountResponse;
import com.background.system.response.OrderCount;
import com.background.system.response.OrderElementResponse;
import com.background.system.response.OrderdResponse;
import com.background.system.response.file.ReadyDownloadFileResponse;
import com.background.system.service.OrderService;
import com.background.system.util.ExcelUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
 *
 * @Author : 志敏.罗
 * @create 2022/8/20 14:53
 */
@Service
public class OrderServiceImpl extends BaseService implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private OrderMapper orderMapper;

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
    @CachePut(value = "order", key = "#orderVo.id")
    public String createOrder(OrderVo orderVo) {
        Orderd order = new Orderd();

        if (StringUtils.isEmpty(orderVo.getCaizhiId())) {
            throw new ServiceException(1003, "请选择材质后在进行操作");
        }

        BeanUtil.copyProperties(orderVo, order);

        MaterialQuality materialQuality = materialQualityService
                .getMaterialQualityDetail(order.getCaizhiId());

        List<OrderElement> orderElements = orderVo.getOrderElements();
        //多个
        BigDecimal total = materialQuality.getPrice();
        //一共买了多少
        int totalNumber = orderElements.stream().mapToInt(OrderElement::getNumber).sum();
        total = total.multiply(BigDecimal.valueOf(totalNumber));

        order.setTotal(total);
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
        Coupon coupon = couponService.getCouponDetail(couponId);

        if (coupon.getIsUsed()) {
            return false;
        }

        // 更新订单
        Orderd order = getOrderDetail(orderId);
        order.setStatus("1");
        order.setIsPay(true);
        order.setCouponId(couponId);
//        orderMapper.updateByPrimaryKeySelective(order);
        updateOrder(order);

        // 优惠券标记使用过
        coupon.setIsUsed(true);
        Token weChatCurrentUser = getWeChatCurrentUser();
        String username = weChatCurrentUser.getUsername();
        coupon.setOpenId(username);
        return couponService.updateService(coupon);
    }

    @Override
    @Transactional
    @CachePut(value = "order", key = "#orderId")
    public Boolean changeAddress(String orderId, String addressId) {
        Orderd order = getOrderDetail(orderId);
        order.setAddressId(addressId);
        return orderMapper.updateById(order) > 0;
    }

    @Override
    public OrderdResponse info(String orderId) {
        OrderdResponse orderResponse = new OrderdResponse();
        Orderd order = getOrderDetail(orderId);
        BeanUtils.copyProperties(order, orderResponse);
        //图片 地址 尺寸 优惠卷
        //elements
        List<OrderElement> elements = elementsMapper.getOrderElementsByOrderId(orderId);
        if (CollectionUtils.isNotEmpty(elements)) {
            List<OrderElementResponse> elementResponses = Lists.newArrayList();
            elements.forEach(orderElement -> {
                OrderElementResponse elementResponse = new OrderElementResponse();
                BeanUtils.copyProperties(orderElement, elementResponse);
                elementResponse.setPictures(pictureService.getPicturesByIds(orderElement.getPictureIds()));
                elementResponses.add(elementResponse);
            });
            orderResponse.setElements(elementResponses);
        }
        if (StringUtils.isNotEmpty(order.getAddressId())) {
            orderResponse.setAddress(addressService.getAddressDetail(order.getAddressId()));
        }

        if (StringUtils.isNotEmpty(order.getSizeId())) {
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
//        orderMapper.selectMapsPage(new Page<>(page, size),
//                new QueryWrapper<Orderd>()
//                        .eq("create_user", currentUser.getUsername()));
        //商品
        List<OrderdResponse> orderResponses = transformOrderResponse(orderList);
        int orderCount = orderMapper.getCurrentOrderCount(currentUser.getUsername());
        orderPage.setTotal(orderCount);
        orderPage.setRecords(orderResponses);
        return orderPage;
    }

    @Override
    @Transactional
    @CachePut(value = "order", key = "#order.id")
    public Boolean updateOrder(Orderd order) {
        //判断优惠卷限制
        if (order.getCouponId() != null && order.getCouponId() != 0) {
            Coupon coupon = couponService.getCouponDetail(order.getCouponId());
            Integer userLimit = coupon.getUseLimit();
            if (order.getTotal() != null && order.getTotal().compareTo(BigDecimal.valueOf(userLimit)) < 0) {
                throw new ServiceException(1004, "您不符合当前优惠卷限制，请重新选择");
            }
        }
        return orderMapper.updateByPrimaryKeySelective(order) > 0;
    }

    @Override
    @Transactional
    public Boolean cancelOrder(String id) {
        logger.info("cancelOrder [{}]", id);
        Token currentUser = getWeChatCurrentUser();
        Orderd order = getOrderDetail(id);
        if (!order.getCreateUser().equals(currentUser.getUsername())) {
            throw new ServiceException(1002, "请修改属于你自己的订单");
        }
        //0-待付款 1-待发货 2-配送中 3-已完成 4-已取消
        return orderMapper.deleteOrderById(id) > 0;
    }

    @Override
    @SuppressWarnings({"all"})
    public Page<OrderdResponse> getAdminOrderList(Integer page, Integer size, Integer type,
                                                  String sizeId, Long orderId, String orderNo, String name) {
        //todo 思路俩个接口 一个数量 一个列表 防止数量过大加载慢
        Page<OrderdResponse> orderPage = initPage(page, size);
        if (type >= 5) {
            throw new ServiceException(1000, "类型暂无处理");
        }
        page = (page - 1) * size;
        //0-待付款  1-待发货 2-配送中 3-已完成 4-已取消
        List<String> sizeIds = Lists.newArrayList();
        if (StringUtils.isNotEmpty(sizeId)) {
            sizeIds = Arrays.asList(sizeId.split(","));
        }

        int count = orderMapper.getOrderCountByType(type, sizeIds, orderId, orderNo, name);
        List<Orderd> orders = orderMapper.getOrderByType(page, size, type, sizeIds, orderId, orderNo, name);
        List<OrderdResponse> orderResponses = transformOrderResponse(orders);
        orderPage.setTotal(count);
        orderPage.setRecords(orderResponses);
        return orderPage;
    }

    @Override
    public Map<String, Integer> getAdminOrderCount() {
        Map<String, Integer> countMap = initCountMap();
        //待付款 待发货 配送中 已取消 已完成 支付金额
        //分组统计 最好设置 state 默认 为 0
        //todo 可以适当改造 前面维护status  0-待付款  1-待发货 2-配送中 3-已完成 4-已取消
        List<OrderCount> orderCount = orderMapper.getOrderCount();

        if (CollectionUtils.isNotEmpty(orderCount)) {
            Integer orderTotalMoney = orderMapper.getOrderTotalMoney();
            Integer actualMoney = orderMapper.getActualMoney();
            Map<String, Integer> orderMap = orderCount.stream()
                    .collect(Collectors.toMap(OrderCount::getStatus, OrderCount::getCount));

            countMap.put("待付款", Optional.ofNullable(orderMap.get("0")).orElse(0));
            countMap.put("待发货", Optional.ofNullable(orderMap.get("1")).orElse(0));
            countMap.put("配送中", Optional.ofNullable(orderMap.get("2")).orElse(0));
            countMap.put("已完成", Optional.ofNullable(orderMap.get("3")).orElse(0));
            countMap.put("已取消", Optional.ofNullable(orderMap.get("4")).orElse(0));
            countMap.put("支付金额", orderTotalMoney);
            //不包括运费
            countMap.put("实际金额", actualMoney);
        }
        return countMap;
    }

    @Override
    @Transactional
    @CachePut(value = "order", key = "#id")
    public Boolean updateKdNo(String id, String orderNo) {
        logger.info("updateKdNo [{}] -- [{}]", id, orderNo);
        checkOrder(id);
        Token currentUser = getCurrentUser();
        return orderMapper.updateKdNo(id, orderNo, currentUser.getUsername()) > 0;
    }

    @Override
    @Transactional
    @CachePut(value = "order", key = "#id")
    public Boolean orderClose(String id) {
        logger.info("orderClose [{}]", id);
        checkOrder(id);
        Token currentUser = getWeChatCurrentUser();
        //0-待付款  1-待发货 2-配送中 3-已完成 4-已取消
        return orderMapper.closeOrder(id, currentUser.getUsername()) > 0;
    }

    @Override
    @CachePut(value = "order", key = "#id")
    public Boolean orderDownload(String id) {
        logger.info("orderDownload[{}]", id);
        return orderMapper.orderDownload(id) > 0;
    }

    @Override
    public Object getOrderCurrentDay() {
        String currentDay = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
        return orderMapper.getIndexOrderCount(currentDay);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map<String, List<Map<String, String>>> orderExport(Integer page,
                                                              Integer size,
                                                              Integer type,
                                                              String sizeId,
                                                              Long orderId,
                                                              String orderNo,
                                                              String name
    ) {
        Page<OrderdResponse> orderdResponsePage = getAdminOrderList(page, size, type, sizeId, orderId, orderNo, name);
        List<OrderdResponse> orderResponses = orderdResponsePage.getRecords();
        Map<String, List<Map<String, String>>> map = Maps.newLinkedHashMap();
        if (CollectionUtils.isNotEmpty(orderResponses)) {
            ExcelUtil util = new ExcelUtil<>();
            List orderFields = util.getAllFields(Orderd.class);
            List addressFields = util.getAllFields(Address.class);
            List sizeFields = util.getAllFields(Size.class);
            List materialQualityFields = util.getAllFields(MaterialQuality.class);
            List orderElementFields = util.getAllFields(OrderElement.class);
            List pictureFields = util.getAllFields(Picture.class);
            List couponFields = util.getAllFields(Coupon.class);

            List<Map<String, String>> orderList = Lists.newArrayList();
            List<Map<String, String>> addressList = Lists.newArrayList();
            List<Map<String, String>> sizeList = Lists.newArrayList();
            List<Map<String, String>> materialQualityList = Lists.newArrayList();
            List<Map<String, String>> orderElementList = Lists.newArrayList();
            List<Map<String, String>> pictureList = Lists.newArrayList();
            List<Map<String, String>> couponList = Lists.newArrayList();

            orderResponses.forEach(orderResponse -> {
                Map orderMap = util.convertToMap(orderResponse, orderFields);
                orderList.add(orderMap);

                Size sizeResponse = orderResponse.getSize();
                if (Objects.nonNull(sizeResponse)) {
                    Map sizeMap = util.convertToMap(sizeResponse, sizeFields);
                    sizeList.add(sizeMap);
                }
            });

            List<OrderElement> orderElements = elementsMapper.getOrderElementsByOrderIds
                    (orderResponses.stream().map(Orderd::getId).collect(Collectors.toList()));
            orderElements.forEach(orderElement -> {
                Map orderElementMap = util.convertToMap(orderElement, orderElementFields);
                orderElementList.add(orderElementMap);
            });

            orderElements.forEach(orderElement -> {
                List<Picture> pictures = pictureService.getPicturesByIds(orderElement.getPictureIds());
                pictures.forEach(picture -> {
                    Map pictureMap = util.convertToMap(picture, pictureFields);
                    pictureList.add(pictureMap);
                });
            });

            List<Address> addresses = addressService.getAddressesByIds
                    (orderResponses.stream().map(Orderd::getAddressId).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(addresses)) {
                addresses.forEach(address -> {
                    Map addressMap = util.convertToMap(address, addressFields);
                    addressList.add(addressMap);
                });
            }

            List<MaterialQuality> materialQualities = materialQualityService
                    .getMaterialListByIds(orderResponses.stream().map(Orderd::getCaizhiId).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(materialQualities)) {
                materialQualities.forEach(materialQuality -> {
                    Map materialQualityMap = util.convertToMap(materialQuality, materialQualityFields);
                    materialQualityList.add(materialQualityMap);
                });
            }

            List<Coupon> coupons = couponService
                    .getCouponListByIds(orderResponses.stream().map(Orderd::getCouponId).filter(Objects::nonNull).collect(Collectors.toList()));

            if (CollectionUtils.isNotEmpty(coupons)) {
                coupons.forEach(coupon -> {
                    Map couponMap = util.convertToMap(coupon, couponFields);
                    couponList.add(couponMap);
                });
            }

            map.put("订单", orderList);
            map.put("订单-元素", orderElementList);
            map.put("商品图片", pictureList);
            map.put("地址", addressList);
            map.put("产品", sizeList);
            map.put("材质", materialQualityList);
            map.put("优惠券", couponList);
        }
        return map;
    }


    @Cacheable(value = "order", key = "#id")
    public Orderd getOrderDetail(String id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    private Map<String, Integer> initCountMap() {
        Map<String, Integer> countMap = new HashMap<>();
        countMap.put("待付款", 0);
        countMap.put("待发货", 0);
        countMap.put("配送中", 0);
        countMap.put("已取消", 0);
        countMap.put("已完成", 0);
        countMap.put("支付金额", 0);
        return countMap;
    }

    private List<OrderdResponse> transformOrderResponse(List<Orderd> orderList) {
        List<OrderdResponse> orderResponses = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(orderList)) {
            //数量转换
            List<CountResponse> countResponses = elementsMapper.
                    getOrderCountByIds(orderList.stream().map(Orderd::getId).collect(Collectors.toList()));

            Map<String, Integer> countMap = countResponses.stream()
                    .collect(Collectors.toMap(CountResponse::getOrderId, CountResponse::getNumber));

            orderList.forEach(order -> {
                OrderdResponse orderResponse = new OrderdResponse();
                BeanUtils.copyProperties(order, orderResponse);
                if (StringUtils.isNotEmpty(order.getSizeId())) {
                    orderResponse.setSize(sizeService.getSizeDetail(order.getSizeId()));
                }

                orderResponse.setNum(countMap.isEmpty() ? 0 : Optional.ofNullable(countMap.get(order.getId())).orElse(0));
                orderResponses.add(orderResponse);
            });
        }
        return orderResponses;
    }

    private void checkOrder(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new ServiceException(1003, "id不可以为空");
        }
        Orderd live = getOrderDetail(id);
        if (live == null) {
            throw new ServiceException(1004, "该订单不存在，请确认后重新操作");
        }
    }

    @Transactional
    public void closeOrder() {
        logger.info("really close and delete orders");
        List<Long> orderIds = orderMapper.getCloseOrderId();
        List<Long> deleteOrderIds = orderMapper.getDeleteOrderIds();
        logger.info("really order close[{}],delete[{}]", orderIds.size(), deleteOrderIds.size());
        if (CollectionUtils.isNotEmpty(orderIds)) {
            this.orderMapper.close(orderIds);
        }
        if (CollectionUtils.isNotEmpty(deleteOrderIds)) {
            this.orderMapper.delete(deleteOrderIds);
        }
    }


    /**
     * 得到没有zip链接的
     *
     * @return 所有待处理对象
     */
    public List<ReadyDownloadFileResponse> getFile(List<String> targets) {

        //找到支付 没有删除的订单 并且没有链接生产的
        List<ReadyDownloadFileResponse> orders = orderMapper.getNoZipPathOrder(targets);
        //orderElements
        if (CollectionUtils.isEmpty(orders)) {
            return Collections.emptyList();
        }

        List<ReadyDownloadFileResponse> targetOrders = Lists.newArrayList();
        Map<String, List<ReadyDownloadFileResponse>> orderMap = orders.stream().collect(Collectors.groupingBy(ReadyDownloadFileResponse::getId));
        orderMap.forEach((k, v) -> {
            List<Picture> pictures = Lists.newArrayList();
            v.forEach(vv -> {
                Map<String, List<Picture>> pictureMap = pictureService.getPicturesByIds(vv.getPictureIds()).stream().collect(Collectors.groupingBy(Picture::getId));
                vv.getPictureIds().forEach(p -> {
                    vv.getPictures().addAll(Optional.ofNullable(pictureMap.get(p)).orElse(new ArrayList<>()));
                });
                pictures.addAll(vv.getPictures());
            });
            ReadyDownloadFileResponse response = ReadyDownloadFileResponse.builder()
                    .id(k)
                    .pictures(pictures)
                    .number(v.stream().mapToInt(ReadyDownloadFileResponse::getNumber).sum())
                    .size(v.stream().findFirst().orElse(new ReadyDownloadFileResponse()).getSize())
                    .sizeName(v.stream().findFirst().orElse(new ReadyDownloadFileResponse()).getSizeName())
                    .face(v.stream().findFirst().orElse(new ReadyDownloadFileResponse()).getFace())
                    .wxNo(v.stream().findFirst().orElse(new ReadyDownloadFileResponse()).getWxNo())
                    .build();
            targetOrders.add(response);
        });

        return targetOrders;
    }
}
