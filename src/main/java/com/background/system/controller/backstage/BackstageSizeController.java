package com.background.system.controller.backstage;

import com.background.system.entity.Size;
import com.background.system.response.SizeResponse;
import com.background.system.service.SizeService;
import com.background.system.util.ExcelUtil;
import com.background.system.util.Result;
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
@ApiOperation("后台产品管理")
public class BackstageSizeController {

    @Autowired
    private SizeService sizeService;
    
    @GetMapping("/export")
    @ApiOperation(value = "导出产品")
    public void export(@RequestParam(value = "page",defaultValue = "1")Integer page,
                       @RequestParam(value = "size",defaultValue = "10")Integer size,
                       HttpServletResponse response)
    {
//        Map<String, List<Map<String, String>>> sizeMap = sizeService.sizeExport(page, size);
//        new ExcelUtil<>().exportExcel(response, sizeMap, "产品",
//                Size.class,
//                MaterialQuality.class,
//                Picture.class);
        new ExcelUtil<>(SizeResponse.class)
                .exportExcel(sizeService.getSizeList(page, size).getRecords(), "产品",response);
    }

    @GetMapping("/detail")
    @ApiOperation("后台-产品详情")
    public Result<?> getSizeDetail(@RequestParam(value = "id")String id)
    {
        return Result.success(sizeService.getSizeDetail(id));
    }

    @PostMapping("/insert")
    @ApiOperation("后台-产品新增")
    public Result<?> sizeInsert(@RequestBody Size size)
    {
        return Result.success(sizeService.sizeInsert(size));
    }

    @DeleteMapping("/delete")
    @ApiOperation("后台-产品删除")
    public Result<?> sizeDelete(@RequestParam String id)
    {
        return Result.success(sizeService.sizeDelete(id));
    }

    @PostMapping("/update")
    @ApiOperation("后台-产品修改")
    public Result<?> sizeUpdate(@RequestBody Size size)
    {
        return Result.success(sizeService.sizeUpdate(size));
    }

}
