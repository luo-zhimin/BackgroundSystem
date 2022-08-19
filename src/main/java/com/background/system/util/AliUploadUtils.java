package com.background.system.util;

import cn.hutool.core.lang.UUID;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/19 2:06 PM
 */
@Slf4j
public class AliUploadUtils {

    private static final String ENDPOINT = "https://oss-cn-hangzhou.aliyuncs.com";
    private static final String RESULTURL = "https://asugar.oss-cn-hangzhou.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAI5tJdGdn4guLATNEwNJft";
    private static final String ACCESS_KEY_SECRET = "C66R67VRkLqihwJnSRD0knnhzkPhja";
    private static final String BUCKET_NAME = "asugar";

    /**
     *
     * @param file  文件流
     * @param father 上级目录（上传哪个文件夹）
     */
    public static String upload(MultipartFile file, String father) {

        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        String type = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        String title = UUID.randomUUID().toString();
        String path = father + "/" + title + type;
        try {
            PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, path, new ByteArrayInputStream(file.getBytes()));
            PutObjectResult putObjectResult = ossClient.putObject(request);
            log.info("[上传信息] - " + putObjectResult);
        } catch (IOException e) {
            return "";
        }finally {
            ossClient.shutdown();
        }
        return RESULTURL + "/" + father + "/" + title + type;
    }
}
