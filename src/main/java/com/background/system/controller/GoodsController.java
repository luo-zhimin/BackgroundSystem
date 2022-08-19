package com.background.system.controller;

import com.background.system.service.GoodsService;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/19 21:11
 */
@Api("商品管理")
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/list")
    @ApiOperation("商品列表")
    public Result<?> getGoodsList(@RequestParam(value = "page",defaultValue = "0")Integer page,
                                  @RequestParam(value = "size",defaultValue = "10")Integer size)
    {
        return Result.success(goodsService.getGoodsList(page,size));
    }
}
