package com.background.system.controller;

import cn.hutool.jwt.JWTUtil;
import com.background.system.annotation.IgnoreLogin;
import com.background.system.constant.Constant;
import com.background.system.entity.Caizhi;
import com.background.system.entity.Coupon;
import com.background.system.entity.Size;
import com.background.system.entity.token.Token;
import com.background.system.entity.vo.AdminLoginVo;
import com.background.system.service.CouponService;
import com.background.system.service.MaterialQualityService;
import com.background.system.service.OrderService;
import com.background.system.service.SizeService;
import com.background.system.service.admin.IAdminUseService;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/17 6:40 PM
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "后台管理")
public class AdminController {
    @Autowired
    private IAdminUseService adminUseService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SizeService sizeService;

    @Autowired
    private MaterialQualityService qualityService;


    /**
     * 登录请求
     * @param loginVo
     * @return
     */
    @IgnoreLogin
    @PostMapping("/login")
    public Result<Token> login(@RequestBody AdminLoginVo loginVo) {
        String userName = loginVo.getUsername();
        String password = loginVo.getPassword();
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            return Result.setData(200, null, "账号和密码都不能为空");
        }
        password = DigestUtils.md5Hex(password);
        Boolean loginFlag = adminUseService.queryUser(userName, password);
        if (!loginFlag) {
            return Result.setData(200, null, "账号或密码错误");
        }
        Token token = createToken(userName, password);
        return Result.success(token);
    }

    private Token createToken(String userName, String password) {
        String tokenKey = userName + password;
        Map<String, Object> params = new HashMap<String, Object>(3) {
            private static final long serialVersionUID = 1L;

            {
                put("user_name", userName);
                put("password", password);
                put("expire_time", Constant.EXPIRE_TIME);
            }
        };
        String tokenValue = JWTUtil.createToken(params, tokenKey.getBytes());

        return Token.builder()
            .token(tokenValue)
            .expireTime(Constant.EXPIRE_TIME)
            .username(userName)
            .password(password)
            .build();
    }

    @GetMapping("/coupon/list")
    @ApiOperation("后台获取消费卷列表")
    public Result<?> getCoupons(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                @RequestParam(value = "size",defaultValue = "10")Integer size)
    {
        return Result.success(couponService.getCouponList(page,size,"admin"));
    }

    @PostMapping("/coupon/insert")
    @ApiOperation(value = "优惠卷新增")
    public Result<?> couponInsert(@RequestBody Coupon coupon){
        return Result.success(couponService.insert(coupon));
    }

    @PostMapping("/coupon/update")
    @ApiOperation(value = "优惠卷修改")
    public Result<?> couponUpdate(@RequestBody Coupon coupon){
        return Result.success(couponService.update(coupon));
    }


    @GetMapping("/order/list")
    @ApiOperation("后台-订单列表")
    public Result<?> getAdminOrderList(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                       @RequestParam(value = "size",defaultValue = "10")Integer size,
                                       @ApiParam(name = "0-待付款  1-待发货 2-配送中 3-已完成 4-已取消",value = "type",defaultValue = "0",required = true) @RequestParam Integer type,
                                       @RequestParam(value = "sizeId",required = false) String sizeId)
    {
        return Result.success(orderService.getAdminOrderList(page,size,type,sizeId));
    }


    @PostMapping("/order/download")
    @ApiOperation("后台-zip下载完回调")
    public Result<?> orderDownload(@RequestParam String id)
    {
        return Result.success(orderService.orderDownload(id));
    }


    @PostMapping("/order/update")
    @ApiOperation("后台-修改快递单号")
    public Result<?> updateKdNo(@RequestParam String  id,
                                @RequestParam String orderNo)
    {
        return Result.success(orderService.updateKdNo(id,orderNo));
    }

    @GetMapping("/order/count")
    @ApiOperation("后台-订单列表-数量统计")
    public Result<?> getAdminOrderCount()
    {
        return Result.success(orderService.getAdminOrderCount());
    }

    @GetMapping("/order/info")
    @ApiOperation("订单详情")
    public Result<?> info (String orderId) {
        return Result.success(orderService.info(orderId));
    }

    @GetMapping("/order/currentDay")
    @ApiOperation("当前日期的消息")
    @IgnoreLogin
    public Result<?> getOrderCurrentDay () {
        return Result.success(orderService.getOrderCurrentDay());
    }


    @PostMapping("/size/insert")
    @ApiOperation("尺寸新增")
    public Result<?> sizeInsert(@RequestBody Size size)
    {
        return Result.success(sizeService.sizeInsert(size));
    }

    @DeleteMapping("/size/delete")
    @ApiOperation("尺寸删除")
    public Result<?> sizeDelete(@RequestParam String id)
    {
        return Result.success(sizeService.sizeDelete(id));
    }

    @PostMapping("/size/update")
    @ApiOperation("尺寸修改")
    public Result<?> sizeUpdate(@RequestBody Size size)
    {
        return Result.success(sizeService.sizeUpdate(size));
    }

    @PostMapping("/material/insert")
    @ApiOperation("材质新增")
    @IgnoreLogin
    public Result<?> materialQualityInsert(@RequestBody Caizhi caizhi)
    {
        return Result.success(qualityService.materialQualityInsert(caizhi));
    }

    @PostMapping("/material/update")
    @ApiOperation("材质修改")
    @IgnoreLogin
    public Result<?> materialQualityUpdate(@RequestBody Caizhi caizhi)
    {
        return Result.success(qualityService.materialQualityUpdate(caizhi));
    }

    @GetMapping("/material/list")
    @ApiOperation("材质列表")
    @IgnoreLogin
    public Result<?> materialQualityList()
    {
        return Result.success(qualityService.getMaterialQualityList());
    }

    @DeleteMapping("/material/delete")
    @ApiOperation("删除材质")
    @IgnoreLogin
    public Result<?> deleteMaterialQuality(@RequestParam Long id)
    {
        return Result.success(qualityService.deleteMaterialQuality(id));
    }
}
