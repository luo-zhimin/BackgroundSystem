package com.background.system.controller;

import com.background.system.annotation.IgnoreLogin;
import com.background.system.entity.Size;
import com.background.system.service.SizeService;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * @Author : 志敏.罗
 * @create 2022/8/19 21:11
 */
@Api(tags = "尺寸管理")
@RestController
@RequestMapping("/size")
public class SizeController {

    @Autowired
    private SizeService sizeService;

    @GetMapping("/page")
    @ApiOperation("尺寸列表-分页")
    @IgnoreLogin
    public Result<?> getSizePage(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                 @RequestParam(value = "size",defaultValue = "10")Integer size)
    {
        return Result.success(sizeService.getSizeList(page,size));
    }

    @GetMapping("/list")
    @ApiOperation("尺寸列表")
    @IgnoreLogin
    public Result<?> getSizeList(Size size)
    {
        return Result.success(sizeService.getSizes(size));
    }


    @GetMapping("/detail")
    @ApiOperation("尺寸详情")
    @IgnoreLogin
    public Result<?> getSizeDetail(@RequestParam(value = "id")String id)
    {
        return Result.success(sizeService.getSizeDetail(id));
    }
}
