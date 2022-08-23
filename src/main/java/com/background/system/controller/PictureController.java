package com.background.system.controller;

import com.background.system.service.PictureService;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/8/23 10:09
 */
@RestController
@RequestMapping("/picture")
@Api(tags = "图库管理")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    /**
     * 上传图片返回图片ID
     * @param file
     * @return
     */
    @PostMapping("getPicture")
    @ApiOperation("上传图片")
    public Result<?> getPicture(@RequestBody MultipartFile file) {
        return Result.success(pictureService.getPicture(file));
    }

}
