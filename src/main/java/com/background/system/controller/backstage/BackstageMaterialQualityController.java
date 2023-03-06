package com.background.system.controller.backstage;

import com.background.system.entity.MaterialQuality;
import com.background.system.service.MaterialQualityService;
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
 * @create 2023/3/3 20:28
 */
@RequestMapping("/admin/material")
@Api(tags = "后台材质")
@RestController
public class BackstageMaterialQualityController {

    @Autowired
    private MaterialQualityService qualityService;

    @PostMapping("/insert")
    @ApiOperation("后台-材质新增")
    public Result<?> materialQualityInsert(@RequestBody MaterialQuality materialQuality)
    {
        return Result.success(qualityService.materialQualityInsert(materialQuality));
    }

    @PostMapping("/update")
    @ApiOperation("后台-材质修改")
    public Result<?> materialQualityUpdate(@RequestBody MaterialQuality materialQuality)
    {
        return Result.success(qualityService.materialQualityUpdate(materialQuality));
    }

    @GetMapping("/list")
    @ApiOperation("材质列表")
    public Result<?> materialQualityList()
    {
        return Result.success(qualityService.getMaterialQualityList());
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除材质")
    public Result<?> deleteMaterialQuality(@RequestParam String id)
    {
        return Result.success(qualityService.deleteMaterialQuality(id));
    }

    @GetMapping("/export")
    @ApiOperation(value = "材质导出")
    public void export(HttpServletResponse response)
    {
        List<MaterialQuality> list = qualityService.getMaterialQualityList();
        ExcelUtil<MaterialQuality> util = new ExcelUtil<>(MaterialQuality.class);
        util.exportExcel(list, "material",response);
    }
}
