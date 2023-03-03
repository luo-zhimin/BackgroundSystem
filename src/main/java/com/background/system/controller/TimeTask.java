package com.background.system.controller;

import com.background.system.annotation.IgnoreLogin;
import com.background.system.util.Result;
import com.background.system.util.SourceZipFileUtils;
import com.background.system.util.ZipFileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:    定时任务
 * @Author: 方糖
 * @Date: 2022/9/3 4:37 PM
 */
@RestController
@RequestMapping("/time/")
public class TimeTask {

    @Resource
    private ZipFileUtils zipFileUtils;

    @Resource
    private SourceZipFileUtils sourceZipFileUtils;

    @GetMapping("zipTask")
    @IgnoreLogin
    public Result<?> task () {

        zipFileUtils.deleteFile.clear();
        zipFileUtils.readyUploadFiles.clear();

        zipFileUtils.cratePictureZip();
        zipFileUtils.uploadZip();

        return Result.success("压缩上传完毕");
    }

    @GetMapping("source/zipTask")
    @IgnoreLogin
    public Result<?> sourceTask (@RequestParam("source") String source, HttpServletResponse response) {

        sourceZipFileUtils.deleteFile.clear();
        sourceZipFileUtils.readyUploadFiles.clear();

        sourceZipFileUtils.cratePictureZip(source);
        sourceZipFileUtils.uploadZip(response);

        return Result.success("sourceZip-压缩上传完毕");
    }

}
