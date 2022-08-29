package com.background.system.controller;

import com.background.system.annotation.IgnoreLogin;
import com.background.system.entity.Caizhi;
import com.background.system.service.MaterialQualityService;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * @Author : 志敏.罗
 * @create 2022/8/20 15:43
 */
@RestController
@RequestMapping("/material")
@Api(tags = "材质管理")
public class MaterialQualityController {

    @Autowired
    private MaterialQualityService qualityService;


    @GetMapping("/list")
    @ApiOperation("材质列表")
    public Result<?> getMaterialQualityList(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                            @RequestParam(value = "size",defaultValue = "10")Integer size)
    {
        return Result.success(qualityService.getMaterialQualityList(page,size));
    }

    @GetMapping("/detail")
    @ApiOperation("材质详情")
    public Result<?> getMaterialQualityDetail(@RequestParam(value = "id")Long id)
    {
        return Result.success(qualityService.getMaterialQualityDetail(id));
    }

    @PostMapping("/insert")
    @ApiOperation("新增")
    @IgnoreLogin
    public Result<?> materialQualityInsert(@RequestBody Caizhi caizhi)
    {
        return Result.success(qualityService.materialQualityInsert(caizhi));
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    @IgnoreLogin
    public Result<?> materialQualityUpdate(@RequestBody Caizhi caizhi)
    {
        return Result.success(qualityService.materialQualityUpdate(caizhi));
    }
}
