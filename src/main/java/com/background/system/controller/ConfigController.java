package com.background.system.controller;

import com.background.system.request.ConfigRequest;
import com.background.system.service.ConfigService;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (config)表控制层
 *
 * @author xxxxx
 */
@RestController
@RequestMapping("/config")
@Api(tags = "参数配置")
public class ConfigController {
    /**
     * 服务对象
     */
    @Resource
    private ConfigService configService;

    @GetMapping("/list")
    @ApiOperation("参数列表")
    public Result<?> getConfigList(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                   @RequestParam(value = "size",defaultValue = "10")Integer size)
    {
        return Result.success(configService.getConfigList(page,size));
    }

    @GetMapping("/detail")
    @ApiOperation("参数详情")
    public Result<?> getConfigDetail(@RequestParam(value = "id")Long id)
    {
        return Result.success(configService.selectByPrimaryKey(id));
    }

    @PostMapping("/insert")
    @ApiOperation("参数详情")
    public Result<?> addConfig(@RequestBody ConfigRequest request)
    {
        return Result.success(configService.addConfig(request));
    }

    @PostMapping("/update")
    @ApiOperation("参数详情")
    public Result<?> updateConfig(@RequestBody ConfigRequest request)
    {
        return Result.success(configService.updateConfig(request));
    }
}
