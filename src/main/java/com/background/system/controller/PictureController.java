package com.background.system.controller;

import com.background.system.annotation.IgnoreLogin;
import com.background.system.service.PictureService;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
//@Scope(value="prototype")
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
    @IgnoreLogin
    public Result<?> getPicture(MultipartFile file) {
        //前端一直循环调取 是否加锁
        return Result.success(pictureService.getPicture(file,"default"));
    }

    @PostMapping("/upload")
    @ApiOperation("批量-上传图片")
    @IgnoreLogin
    public Result<?> upload(MultipartFile[] file) {
        return Result.success(pictureService.upload(file,"default"));
    }

    @PostMapping("uploadIndex")
    @ApiOperation("上传首页图片")
    @IgnoreLogin
    public Result<?> uploadIndex(MultipartFile file) {
        return Result.success(pictureService.getPicture(file,"index"));
    }

    @GetMapping("getIndexPicture")
    @ApiOperation("获取首页大图")
    @IgnoreLogin
    public Result<?> getIndexPicture() {
        return Result.success(pictureService.getIndexPicture());
    }
}
