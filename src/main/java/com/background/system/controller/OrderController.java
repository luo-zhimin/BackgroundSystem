package com.background.system.controller;

import com.background.system.entity.Order;
import com.background.system.entity.vo.OrderVo;
import com.background.system.service.OrderService;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
     * @param order
     * @return
     */
    @PostMapping("update")
    @ApiOperation("更新订单-地址和优惠券和运费")
    public Result<?> update(@RequestBody Order order) {
        return Result.success(orderService.updateOrder(order));
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

    @GetMapping("/admin/list")
    @ApiOperation("后台-订单列表")
    public Result<?> getAdminOrderList(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                       @RequestParam(value = "size",defaultValue = "10")Integer size,
                                       @ApiParam(name = "待付款，待发货，售后订单，交易关闭 ...",value = "type",defaultValue = "0",readOnly = true) @RequestParam String type)
    {
        return Result.success(orderService.getAdminOrderList(page,size,type));
    }

    @GetMapping("/admin/count")
    @ApiOperation("后台-订单列表-数量统计")
    public Result<?> getAdminOrderCount()
    {
        return Result.success(orderService.getAdminOrderCount());
    }
}
