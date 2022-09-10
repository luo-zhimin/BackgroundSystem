package com.background.system.util;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.background.system.cache.ConfigCache;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
public class AliUploadUtils {

    private static final Logger logger = LoggerFactory.getLogger(AliUploadUtils.class);

    private static final String ENDPOINT;

    private static final String RESULT_URL;

    private static final String ACCESS_KEY_ID;

    private static final String ACCESS_KEY_SECRET;

    private static final Integer ZERO = 0;

    private static final Integer ONE_ZERO_TWO_FOUR = 1024;

    private static final Integer NINE_ZERO_ZERO = 900;

    private static final Integer THREE_TWO_SEVEN_FIVE = 3275;

    private static final Integer TWO_ZERO_FOUR_SEVEN = 2047;

    private static final Double ZERO_EIGHT_FIVE = 0.85;

    private static final Double ZERO_SIX = 0.6;

    private static final Double ZERO_FOUR_FOUR = 0.44;

    private static final Double ZERO_FOUR = 0.4;

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
        logger.info("file name[{}]",file.getOriginalFilename());
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        //名字 后缀会重叠
        //default 多个文件 前端循环
        String title = father.equals("zip") ? file.getOriginalFilename() : System.currentTimeMillis() + "-" + file.getOriginalFilename();//zh_cn path name
        String path = father+File.separator +title;
        try {
            PutObjectRequest request =
                new PutObjectRequest(BUCKET_NAME, path, new ByteArrayInputStream(file.getBytes()));
            PutObjectResult putObjectResult = ossClient.putObject(request);
            PushPlusUtils.push(putObjectResult);
            logger.info("[上传信息] - " + JSON.toJSONString(putObjectResult));
        } catch (IOException e) {
            return "";
        } finally {
            ossClient.shutdown();
        }
        return RESULT_URL + "/" + father + "/" + title ;
    }


    //todo 线程池异步处理
//    @SneakyThrows
    public static Map<String,String> uploadImages(MultipartFile[] files, String father) {
        Map<String,String> pictureMap = new ConcurrentHashMap<>();
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        long start = System.currentTimeMillis();
        for (MultipartFile file : files) {
            String title = father.equals("zip") ? file.getOriginalFilename() : System.currentTimeMillis() + "-" + file.getOriginalFilename();//zh_cn path name
            String path = father + File.separator + title;

            PutObjectRequest request = null;
            try {
                //图片压缩
                byte[] bytes = compressPicForScale(file.getBytes(), 120);
                request = new PutObjectRequest(BUCKET_NAME, path, new ByteArrayInputStream(bytes));
            } catch (IOException e) {
                logger.error("图片上传失败[{}]",e.getMessage());
            }
            //todo 慢
            PutObjectResult putObjectResult = ossClient.putObject(request);
            PushPlusUtils.push(putObjectResult);
            pictureMap.put(title, RESULT_URL + File.separator + father + File.separator + title);
        }
        ossClient.shutdown();
        long end = System.currentTimeMillis();
        logger.info("need time [{}]",end-start);
        logger.info("[上传信息] - " + JSON.toJSONString(pictureMap));
        return pictureMap;
    }

    /**
     * 上传文件
     * @param file 文件
     * @param path 路径
     * @return 结果路径
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

    /**
     * 根据指定大小压缩图片
     * @param imageBytes 源图片字节数组
     * @param desFileSize 指定图片大小，单位kb
     * @return 压缩质量后的图片字节数组
     */
    public static byte[] compressPicForScale(byte[] imageBytes, long desFileSize) {
        if (imageBytes == null || imageBytes.length <= ZERO || imageBytes.length < desFileSize * ONE_ZERO_TWO_FOUR) {
            return imageBytes;
        }
        long srcSize = imageBytes.length;
        double accuracy = getAccuracy(srcSize / ONE_ZERO_TWO_FOUR);
        try {
            while (imageBytes.length > desFileSize * ONE_ZERO_TWO_FOUR) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(inputStream)
                        .scale(accuracy)
                        .outputQuality(accuracy)
                        .toOutputStream(outputStream);
                imageBytes = outputStream.toByteArray();
            }
            logger.info("图片原大小={}kb | 压缩后大小={}kb | 压缩倍数={}",
                    srcSize / ONE_ZERO_TWO_FOUR, imageBytes.length / ONE_ZERO_TWO_FOUR,accuracy);
        } catch (Exception e) {
            logger.error("【图片压缩】msg=图片压缩失败!", e);
        }
        return imageBytes;
    }

    /**
     * 自动调精度
     * @param size 源文件大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < NINE_ZERO_ZERO) {
            accuracy = ZERO_EIGHT_FIVE;
        } else if (size < TWO_ZERO_FOUR_SEVEN) {
            accuracy = ZERO_SIX;
        } else if (size < THREE_TWO_SEVEN_FIVE) {
            accuracy = ZERO_FOUR_FOUR;
        } else {
            accuracy = ZERO_FOUR;
        }
        return accuracy;
    }
}
