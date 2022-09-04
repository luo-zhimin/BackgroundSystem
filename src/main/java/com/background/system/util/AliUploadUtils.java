package com.background.system.util;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/19 2:06 PM
 */
@Slf4j
public class AliUploadUtils {

    private static final String ENDPOINT = "https://oss-cn-hangzhou.aliyuncs.com";
    private static final String RESULT_URL = "https://asugar.oss-cn-hangzhou.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAI5tJdGdn4guLATNEwNJft";
    private static final String ACCESS_KEY_SECRET = "C66R67VRkLqihwJnSRD0knnhzkPhja";
    private static final String BUCKET_NAME = "asugar";

    /**
     * @param file   文件流
     * @param father 上级目录（上传哪个文件夹）
     */
    public static String uploadImage(MultipartFile file, String father) {
        log.info("file name[{}]",file.getOriginalFilename());
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        //名字 后缀会重叠
//        String type =
//            Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        String title = file.getOriginalFilename();//zh_cn path name
//        String path = father + "/" + title + type;
        String path = father + "/" + title;
        try {
            PutObjectRequest request =
                new PutObjectRequest(BUCKET_NAME, path, new ByteArrayInputStream(file.getBytes()));
            PutObjectResult putObjectResult = ossClient.putObject(request);
            PushPlusUtils.push(putObjectResult);
            log.info("[上传信息] - " + JSON.toJSONString(putObjectResult));
        } catch (IOException e) {
            return "";
        } finally {
            ossClient.shutdown();
        }
//        return RESULT_URL + "/" + father + "/" + title + type;
        return RESULT_URL + "/" + father + "/" + title ;
    }

    /**
     * 上传文件
     *
     * @param file
     * @param path
     * @return
     */
    public static String uploadPdf(File file, String path) {
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        try {

            PushPlusUtils.push(ossClient.putObject(BUCKET_NAME, path, file));
        } finally {
            ossClient.shutdown();
        }

        return RESULT_URL + "/" + path;
    }
}
