package com.background.system.util;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.background.system.cache.ConfigCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.background.system.constant.Constant.BUCKET_NAME;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/19 2:06 PM
 */
@Slf4j
public class AliUploadUtils {

    private static final String ENDPOINT;
    private static final String RESULT_URL;
    private static final String ACCESS_KEY_ID;
    private static final String ACCESS_KEY_SECRET;

    static {
        ENDPOINT = ConfigCache.configMap.get("ENDPOINT");
        RESULT_URL = ConfigCache.configMap.get("RESULT_URL");
        ACCESS_KEY_ID = ConfigCache.configMap.get("ACCESS_KEY_ID");
        ACCESS_KEY_SECRET = ConfigCache.configMap.get("ACCESS_KEY_SECRET");
    }

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
        return RESULT_URL + "/" + father + "/" + title ;
    }


    //todo 线程池异步处理
    public static Map<String,String> uploadImages(MultipartFile[] files, String father) {
        Map<String,String> pictureMap = new ConcurrentHashMap<>();
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        //名字 后缀会重叠
        for (MultipartFile file : files) {
            String title = file.getOriginalFilename()+"-"+System.currentTimeMillis();//zh_cn path name
            String path = father + "/" + title;
            try {
                PutObjectRequest request =
                        new PutObjectRequest(BUCKET_NAME, path, new ByteArrayInputStream(file.getBytes()));
                PutObjectResult putObjectResult = ossClient.putObject(request);
                PushPlusUtils.push(putObjectResult);
                pictureMap.put(title,RESULT_URL + "/" + father + "/" + title);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ossClient.shutdown();
        log.info("[上传信息] - " + JSON.toJSONString(pictureMap));
        return pictureMap;
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
