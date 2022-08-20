package com.background.system.controller;

import com.background.system.request.AddressRequest;
import com.background.system.service.AddressService;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/20 11:52
 */
@Api("地址管理")
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/insert")
    @ApiOperation("新增地址")
    public Result<?> insertAddress(@RequestBody AddressRequest request)
    {
        return Result.success(addressService.insertAddress(request));
    }

    @PostMapping("/update")
    @ApiOperation("修改地址")
    public Result<?> updateAddress(@RequestBody AddressRequest request)
    {
        return Result.success(addressService.updateAddress(request));
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除地址")
    public Result<?> deleteAddress(@RequestParam(value = "id")Long id)
    {
        return Result.success(addressService.deleteAddress(id));
    }

    @GetMapping("/list")
    @ApiOperation("地址列表")
    public Result<?> getAddressList(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                    @RequestParam(value = "size",defaultValue = "10")Integer size)
    {
        return Result.success(addressService.getAddressList(page,size));
    }

    @GetMapping("/detail")
    @ApiOperation("地址详情")
    public Result<?> getAddressDetail(@RequestParam(value = "id")Long id)
    {
        return Result.success(addressService.getAddressDetail(id));
    }

    @PostMapping("/default")
    @ApiOperation("设置为默认地址")
    public Result<?> updateDefaultAddress(@RequestParam(value = "id")Long id)
    {
        return Result.success(addressService.updateDefaultAddress(id));
    }
}
