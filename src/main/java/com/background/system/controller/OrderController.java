package com.background.system.controller;

import com.background.system.entity.vo.OrderVo;
import com.background.system.service.OrderService;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result<?> changeAddress(String orderId, Long addressId) {
        return Result.success(orderService.changeAddress(orderId,addressId));
    }
}
