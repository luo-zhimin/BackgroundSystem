package com.background.system.controller;

import com.background.system.service.PayService;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/24 6:42 PM
 */
@Api(tags = "支付管理")
@RestController
@RequestMapping("/jsapi/pay")
public class PayController{

    @Autowired
    private PayService payService;

    @GetMapping("/createNo")
    @ApiOperation("JSAPI下单")
    public Result<?> createPay(String orderId){
        return Result.success(payService.createPay(orderId));
    }


    @ApiOperation("唤醒支付需要的paySign")
    @GetMapping("/wakePay")
    public Result<?> wakePay(String prepay_id) {
        return Result.success(payService.wakePay(prepay_id));
    }

    @ApiOperation("支付成功后调这个")
    @GetMapping("/payOk")
    public Result<?> payOk(String orderId) {
        return Result.success(payService.payOk(orderId));
    }
}
