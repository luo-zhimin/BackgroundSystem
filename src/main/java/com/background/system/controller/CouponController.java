package com.background.system.controller;

import com.background.system.entity.Coupon;
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
                                   @RequestParam(value = "size",defaultValue = "10")Integer size)
    {
        return Result.success(couponService.getCouponList(page,size,"wechat"));
    }

    @GetMapping("/detail")
    @ApiOperation("消费卷详情")
    public Result<?> getGoodsDetail(@RequestParam(value = "id")Long id)
    {
        return Result.success(couponService.getCouponDetail(id));
    }

    @PostMapping("/covert")
    @ApiOperation(value = "兑换优惠卷")
    public Result<?> covertCoupon(@RequestBody BaseRequest request){
        return Result.success(couponService.covertCoupon(request));
    }

    @PostMapping("/insert")
    @ApiOperation(value = "新增")
    public Result<?> insert(@RequestBody Coupon coupon){
        return Result.success(couponService.insert(coupon));
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改")
    public Result<?> update(@RequestBody Coupon coupon){
        return Result.success(couponService.update(coupon));
    }

    @GetMapping("/admin/list")
    @ApiOperation("后台获取消费卷列表")
    public Result<?> getCoupons(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                @RequestParam(value = "size",defaultValue = "10")Integer size)
    {
        return Result.success(couponService.getCouponList(page,size,"admin"));
    }
}
