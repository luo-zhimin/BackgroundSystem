package com.background.system.controller.backstage;

import com.background.system.entity.*;
import com.background.system.service.OrderService;
import com.background.system.util.ExcelUtil;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/3 20:28
 */
@RequestMapping("admin/order")
@RestController
@Api(tags = "后台订单管理")
public class BackstageOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    @ApiOperation("后台-订单列表")
    public Result<?> getAdminOrderList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @ApiParam(name = "0-待付款  1-待发货 2-配送中 3-已完成 4-已取消", value = "type", defaultValue = "0", required = true) @RequestParam Integer type,
                                       @RequestParam(value = "sizeId", required = false) String sizeId,
                                       @RequestParam(value = "orderId", required = false) Long orderId,
                                       @RequestParam(value = "orderNo", required = false) String orderNo,
                                       @RequestParam(value = "name", required = false) String name) {
        return Result.success(orderService.getAdminOrderList(page, size, type, sizeId, orderId, orderNo, name));
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出订单")
    public void export(@RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                       @ApiParam(name = "0-待付款  1-待发货 2-配送中 3-已完成 4-已取消", value = "type", defaultValue = "0", required = true) @RequestParam Integer type,
                       @RequestParam(value = "sizeId", required = false) String sizeId,
                       @RequestParam(value = "orderId", required = false) Long orderId,
                       @RequestParam(value = "orderNo", required = false) String orderNo,
                       @RequestParam(value = "name", required = false) String name,
                       HttpServletResponse response)
    {
        Map<String, List<Map<String, String>>> orderMap = orderService.orderExport(page, size, type, sizeId, orderId, orderNo, name);
        new ExcelUtil<>().exportExcel(response, orderMap, "订单",
                Orderd.class,
                OrderElement.class,
                Picture.class,
                Address.class,
                Size.class,
                MaterialQuality.class,
                Coupon.class);
    }

    @PostMapping("/download")
    @ApiOperation("后台-zip下载完回调")
    public Result<?> orderDownload(@RequestParam String id) {
        return Result.success(orderService.orderDownload(id));
    }

    @PostMapping("/update")
    @ApiOperation("后台-修改快递单号")
    public Result<?> updateKdNo(@RequestParam String id,
                                @RequestParam String orderNo) {
        return Result.success(orderService.updateKdNo(id, orderNo));
    }

    @GetMapping("/count")
    @ApiOperation("后台-订单列表-数量统计")
    public Result<?> getAdminOrderCount() {
        return Result.success(orderService.getAdminOrderCount());
    }

    @GetMapping("/info")
    @ApiOperation("订单详情")
    public Result<?> info(String orderId) {
        return Result.success(orderService.info(orderId));
    }

    @GetMapping("/currentDay")
    @ApiOperation("当日订单的销售情况")
    public Result<?> getOrderCurrentDay() {
        return Result.success(orderService.getOrderCurrentDay());
    }
}
