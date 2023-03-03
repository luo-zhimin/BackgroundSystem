package com.background.system.controller.backstage;

import com.background.system.entity.Size;
import com.background.system.response.SizeResponse;
import com.background.system.service.SizeService;
import com.background.system.util.ExcelUtil;
import com.background.system.util.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/3 20:27
 */
@RequestMapping("admin/size")
@RestController
@ApiOperation("后台尺寸管理")
public class BackstageSizeController {

    @Autowired
    private SizeService sizeService;
    
    @GetMapping("/export")
    @ApiOperation(value = "导出尺寸")
    public void export(@RequestParam(value = "page",defaultValue = "1")Integer page,
                       @RequestParam(value = "size",defaultValue = "10")Integer size,
                       HttpServletResponse response)
    {
        Page<SizeResponse> sizePage = sizeService.getSizeList(page, size);
        ExcelUtil<SizeResponse> util = new ExcelUtil<>(SizeResponse.class);
        util.exportExcel(sizePage.getRecords(), "size",response);
    }

    @GetMapping("/detail")
    @ApiOperation("后台-尺寸详情")
    public Result<?> getSizeDetail(@RequestParam(value = "id")String id)
    {
        return Result.success(sizeService.getSizeDetail(id));
    }

    @PostMapping("/insert")
    @ApiOperation("后台-尺寸新增")
    public Result<?> sizeInsert(@RequestBody Size size)
    {
        return Result.success(sizeService.sizeInsert(size));
    }

    @DeleteMapping("/delete")
    @ApiOperation("后台-尺寸删除")
    public Result<?> sizeDelete(@RequestParam String id)
    {
        return Result.success(sizeService.sizeDelete(id));
    }

    @PostMapping("/update")
    @ApiOperation("后台-尺寸修改")
    public Result<?> sizeUpdate(@RequestBody Size size)
    {
        return Result.success(sizeService.sizeUpdate(size));
    }

}
