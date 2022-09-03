package com.background.system.controller;

import com.background.system.annotation.IgnoreLogin;
import com.background.system.util.Result;
import com.background.system.util.ZipFileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description:    定时任务
 * @Author: 方糖
 * @Date: 2022/9/3 4:37 PM
 */
@RestController
@RequestMapping("time")
public class TimeTask {

    @Resource
    private ZipFileUtils zipFileUtils;

    @GetMapping("zipTask")
    @IgnoreLogin
    public Result<?> task () {

        zipFileUtils.deleteFile.clear();
        zipFileUtils.readyUploadFiles.clear();

        zipFileUtils.cratePictureZip();
        zipFileUtils.uploadZip();

        return Result.success("压缩上传完毕");
    }

}
