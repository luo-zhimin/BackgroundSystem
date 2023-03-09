package com.background.system.controller;

import com.background.system.annotation.IgnoreLogin;
import com.background.system.entity.Orderd;
import com.background.system.entity.vo.OrderVo;
import com.background.system.mapper.OrderMapper;
import com.background.system.service.OrderService;
import com.background.system.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/20 3:15 PM
 */
@Api(tags = "订单管理")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderMapper orderMapper;

    /**
     * 返回订单号
     * @param orderVo
     * @return
     */
    @PostMapping("create")
    @ApiOperation("创建订单")
    public Result<?> createOrder(@RequestBody OrderVo orderVo) {
        return Result.success(orderService.createOrder(orderVo));
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @GetMapping("info")
    @ApiOperation("订单详情")
    public Result<?> info (String orderId) {
        return Result.success(orderService.info(orderId));
    }

    /**
     * 更新订单的地址和优惠券
     * @param orderd
     * @return
     */
    @PostMapping("update")
    @ApiOperation("更新订单-地址和优惠券和运费")
    public Result<?> update(@RequestBody Orderd orderd) {
        return Result.success(orderService.updateOrder(orderd));
    }


    /**
     * 返回优惠后的金额
     * @param couponId
     * @param orderId
     * @return
     */
    @GetMapping("coupon")
    @ApiOperation("使用优惠券")
    public Result<?> coupon(Long couponId, String orderId) {
        return Result.success(orderService.coupon(couponId,orderId));
    }

    /**
     * 订单界面修改地址（选择地址列表已有里的地址，新建修改调用地址接口去）
     * @param orderId
     * @param addressId
     * @return
     */
    @GetMapping("changeAddress")
    @ApiOperation("修改地址-选择列表已有的")
    public Result<?> changeAddress(String orderId, String addressId) {
        return Result.success(orderService.changeAddress(orderId,addressId));
    }

    @GetMapping("/list")
    @ApiOperation("获取当前用户的订单记录")
    public Result<?> getOrderList(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                  @RequestParam(value = "size",defaultValue = "10")Integer size)
    {
        return Result.success(orderService.getOrderList(page,size));
    }


    @DeleteMapping("/cancel")
    @ApiOperation("取消订单-取消自己的")
    public Result<?> cancelOrder(@RequestParam(value = "id")String id)
    {
        return Result.success(orderService.cancelOrder(id));
    }

    @PostMapping("/close")
    @ApiOperation("订单确认完成")
    public Result<?> orderClose(@RequestParam String id)
    {
        return Result.success(orderService.orderClose(id));
    }

    @GetMapping("todayOrderOrPrice")
    @ApiOperation("今日订单数-金额数")
    @IgnoreLogin
    public Result<?> todayOrderOrPrice(String type) {
        //直接sql拼接最好
        // 当天订单信息
        QueryWrapper<Orderd> wrapper = new QueryWrapper<>();
        wrapper.apply(true, "TO_DAYS(NOW())-TO_DAYS(create_time) = 0");
        List<Orderd> orders = orderMapper.selectList(wrapper);

        // init - 当前月-月天数
        int []timeCount = new int[25];
        double []priceCount = new double[25];

        // deal
        for (Orderd order : orders) {
            // 订单
            LocalDateTime createTime = order.getCreateTime();
            int hour = createTime.getHour();
            // 价格
            double total = order.getTotal().doubleValue();
            if (hour >= 6) {
                timeCount[hour]++;
                priceCount[hour] += total;
            }
        }
        // 通用返回
        ArrayList<Object> integers = new ArrayList<>(24);
        // 筛选
        switch (type) {
            case "0":
                for (int i = 6 ; i <= 24 ; i++) integers.add(timeCount[i]);
                break;
            case "1":
                for (int i = 6 ; i <= 24 ; i++) integers.add(priceCount[i]);
        }
        return Result.success(integers);
    }

    @GetMapping("weekOrderOrPrice")
    @ApiOperation("每周订单数-金额数")
    @IgnoreLogin
    public Result<?> weekOrderOrPrice(String type) {
        // 每周订单信息
        QueryWrapper<Orderd> wrapper = new QueryWrapper<>();
        wrapper.apply(true, "TO_DAYS(NOW())-TO_DAYS(create_time) <= 7");
        List<Orderd> orders = orderMapper.selectList(wrapper);

        // init
        int []timeCount = new int[8];
        double []priceCount = new double[8];

        // deal
        for (Orderd order : orders) {
            // 订单
            LocalDateTime createTime = order.getCreateTime();
            int value = createTime.getDayOfWeek().getValue();
            // 价格
            double total = order.getTotal().doubleValue();
            timeCount[value]++;
            priceCount[value] += total;
        }

        // 通用返回
        ArrayList<Object> integers = new ArrayList<>(7);

        // 筛选
        switch (type) {
            case "0":
                for (int i = 1 ; i <= 7 ; i++) integers.add(timeCount[i]);
                break;
            case "1":
                for (int i = 1 ; i <= 7 ; i++) integers.add(priceCount[i]);
        }
        return Result.success(integers);
    }
}
