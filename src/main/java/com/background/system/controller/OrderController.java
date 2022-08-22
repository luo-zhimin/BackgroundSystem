package com.background.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.background.system.entity.*;
import com.background.system.entity.vo.OrderVo;
import com.background.system.mapper.*;
import com.background.system.util.AliUploadUtils;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/20 3:15 PM
 */
@Api(tags = "订单管理")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private SizeMapper sizeMapper;

    @Resource
    private CaizhiMapper caizhiMapper;

    @Resource
    private CouponMapper couponMapper;

    @Resource
    private PictureMapper pictureMapper;

    /**
     * 返回订单号
     * @param orderVo
     * @return
     */
    @PostMapping("create")
    @ApiOperation("创建订单")
    public Result<?> createOrder(@RequestBody OrderVo orderVo) {
        Order order = new Order();
        BeanUtil.copyProperties(orderVo, order);
        // 计算价格
        Size size = sizeMapper.selectById(order.getSizeId());
        Caizhi caizhi = caizhiMapper.selectById(order.getCaizhiId());
        BigDecimal uPrice = size.getUPrice();
        BigDecimal price = caizhi.getPrice();
        order.setTotal(uPrice.add(price));
        orderMapper.insert(order);
        return Result.success(order.getId());
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
        Coupon coupon = couponMapper.selectById(couponId);
        BigDecimal price = coupon.getPrice();
        Order order = orderMapper.selectById(orderId);
        BigDecimal total = order.getTotal();
        BigDecimal newTotal = total.subtract(price);
        if (newTotal.compareTo(BigDecimal.ZERO) <= 0) {
            newTotal = BigDecimal.ZERO;
        }
        order.setTotal(newTotal);
        order.setCouponId(couponId);
        orderMapper.updateById(order);
        return Result.success(newTotal);
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
        Order order = orderMapper.selectById(orderId);
        order.setAddressId(addressId);
        return Result.success(orderMapper.updateById(order));
    }

    /**
     * 上传图片返回图片ID
     * @param file
     * @return
     */
    @PostMapping("getPicture")
    @ApiOperation("上传图片")
    public Result<?> getPicture(@RequestBody MultipartFile file) {
        String aDefault = AliUploadUtils.uploadImage(file, "default");
        Picture picture = new Picture();
        picture.setUrl(aDefault);
        picture.setIsDel(false);
        picture.setCreateTime(LocalDateTime.now());
        int insert = pictureMapper.insert(picture);
        return Result.success(picture.getId());
    }

}
