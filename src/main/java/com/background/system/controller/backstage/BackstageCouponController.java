package com.background.system.controller.backstage;

import com.background.system.entity.Coupon;
import com.background.system.service.CouponService;
import com.background.system.util.ExcelUtil;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/3 20:27
 */
@RequestMapping("admin/coupon")
@RestController
@Api(tags = "后台优惠卷管理")
public class BackstageCouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping("/list")
    @ApiOperation("后台获取消费卷列表")
    public Result<?> getCoupons(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                @RequestParam(value = "size",defaultValue = "10")Integer size,
                                @RequestParam(value = "isUse",required = false)Boolean isUse,
                                @RequestParam(value = "status",required = false)Boolean status)
    {
        return Result.success(couponService.getCouponList(page,size,"admin",isUse,status));
    }

    @PostMapping("/insert")
    @ApiOperation(value = "优惠卷新增")
    public Result<?> couponInsert(@RequestBody Coupon coupon){
        return Result.success(couponService.insert(coupon));
    }

    @PostMapping("/update")
    @ApiOperation(value = "优惠卷修改")
    public Result<?> couponUpdate(@RequestBody Coupon coupon){
        return Result.success(couponService.update(coupon));
    }


    @GetMapping("/export")
    @ApiOperation(value = "导出优惠卷")
    public void export(Coupon coupon, HttpServletResponse response)
    {
        List<Coupon> list = couponService.selectCoupons(coupon);
        ExcelUtil<Coupon> util = new ExcelUtil<>(Coupon.class);
        util.exportExcel(list, "coupon",response);
    }
}
