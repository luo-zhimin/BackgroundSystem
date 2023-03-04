package com.background.system.util;

import com.alibaba.fastjson.JSON;
import com.background.system.response.file.FileUploadResponse;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.Closeable;
import java.nio.charset.StandardCharsets;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/4 17:30
 */
@Component
public class FileUploadUtil {

    private Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);

    public Result<?> upload(FileUploadResponse obj, String uploadUrl, String token) {

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(uploadUrl + "/upload");
            ContentType c = ContentType.parse(obj.getContentType());
            //处理ie上传文件名带上全路径的情况
            String fileName = obj.getFileName();
            int pos = fileName.lastIndexOf("\\");
            if(pos != -1){
                fileName = fileName.substring(pos + 1);
            }

            InputStreamBody in = new InputStreamBody(obj.getIn(), c, fileName);
            httpPost.setHeader("token", token);
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .setCharset(StandardCharsets.UTF_8)
                    .addPart("file", in);
            if (obj.getDir() != null) {
                StringBody dirParam = new StringBody(obj.getDir(), ContentType.TEXT_PLAIN);
                entityBuilder.addPart("dir", dirParam);
            }
            if (obj.getRename() != null) {
                StringBody renameParam = new StringBody(String.valueOf(obj.getRename()), ContentType.TEXT_PLAIN);
                entityBuilder.addPart("rename", renameParam);
            }

            HttpEntity reqEntity = entityBuilder.build();
            httpPost.setEntity(reqEntity);

            response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            String s = EntityUtils.toString(resEntity, StandardCharsets.UTF_8);
            // 销毁
            EntityUtils.consume(resEntity);
            logger.info("upload result:{}",s);
            return JSON.parseObject(s, Result.class);
        } catch (Exception e) {
            logger.error("上传出错", e);
            return Result.error(e.getMessage());
        } finally {
            close(response);
            close(httpClient);
        }
    }

    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                //ignore close exception
            }
        }
    }

    @SneakyThrows
    public FileUploadResponse getFileUploadResponse(MultipartFile file, String dir, Boolean rename) {
        return FileUploadResponse.builder()
                .contentType(file.getContentType())
                .fileName(file.getOriginalFilename())
                .in(file.getInputStream())
                .dir(dir)
                .rename(rename)
                .build();
    }
}
