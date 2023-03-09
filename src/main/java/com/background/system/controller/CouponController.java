package com.background.system.controller;

import com.background.system.annotation.IgnoreLogin;
import com.background.system.request.BaseRequest;
import com.background.system.service.CouponService;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * @Author : 志敏.罗
 * @create 2022/8/20 16:11
 */
@RestController
@RequestMapping("/coupon")
@Api(tags = "消费卷")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping("/list")
    @ApiOperation("小程序获取当前用户消费卷列表")
    public Result<?> getCouponList(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                   @RequestParam(value = "size",defaultValue = "10")Integer size,
                                   @RequestParam(value = "isUse",required = false)Boolean isUse,
                                   @RequestParam(value = "status",required = false)Boolean status)
    {
        return Result.success(couponService.getCouponList(page,size,"wechat",isUse,status));
    }

    @GetMapping("/detail")
    @ApiOperation("消费卷详情")
    public Result<?> getGoodsDetail(@RequestParam(value = "id")Long id)
    {
        return Result.success(couponService.getCouponDetail(id));
    }

    @PostMapping("/covert")
    @ApiOperation(value = "兑换优惠卷")
    @IgnoreLogin
    public Result<?> covertCoupon(@RequestBody BaseRequest request){
        return Result.success(couponService.covertCoupon(request));
    }
}
