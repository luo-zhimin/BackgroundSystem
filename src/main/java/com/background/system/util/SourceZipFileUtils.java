package com.background.system.util;

import com.alibaba.fastjson.JSON;
import com.background.system.config.ApplicationContextProvider;
import com.background.system.entity.Picture;
import com.background.system.response.PictureResponse;
import com.background.system.response.file.HandleFile;
import com.background.system.response.file.ReadyDownloadFileResponse;
import com.background.system.response.file.ReadyUploadFile;
import com.background.system.response.file.UploadZipFileResponse;
import com.background.system.service.impl.OrderServiceImpl;
import com.background.system.service.impl.PictureServiceImpl;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.hutool.core.util.ZipUtil.zip;

/**
 * Created by IntelliJ IDEA.
 * 处理文件
 *
 * @Author : 镜像
 * @create 2022/9/3 01:10
 */
@Service
public class SourceZipFileUtils {

    private final Logger logger = LoggerFactory.getLogger(SourceZipFileUtils.class);

    @Value("${zip.path}")
    private String acceptFilePath;

    /**
     * 准备删除的文件
     */
    public List<File> deleteFile = Lists.newArrayList();

    public List<String> picList = new ArrayList<>();

    public List<ReadyUploadFile> readyUploadFiles = Lists.newArrayList();

    public static List<String> errorPictureAddress = new ArrayList<>();

    @Resource
    private ZipFileUtils zipFileUtils;

    /**
     * 订单图片处理 压缩zip 上传服务器
     */
    @SneakyThrows
    public void cratePictureZip(String orderIds) {

        List<String> targets = CommonUtils.getTargets(orderIds);

        if (CollectionUtils.isEmpty(targets)) {
            logger.info("无待处理文件");
            return;
        }

        // init
        picList.clear();

        //扫描前创建扫描目录
        zipFileUtils.judgeFileExists(Lists.newArrayList(acceptFilePath));

        List<ReadyDownloadFileResponse> readyDownloadFIleResponses = ApplicationContextProvider
                .getBean(OrderServiceImpl.class).getFile(targets);

        if (CollectionUtils.isEmpty(readyDownloadFIleResponses)) {
            logger.info("无待处理文件");
            return;
        }

        for (ReadyDownloadFileResponse response : readyDownloadFIleResponses) {
            if (CollectionUtils.isEmpty(response.getPictures())) {
                logger.info("该订单没有照片--" + response.getId());
                continue;
            }

            // init
            picList.clear();

            //一个订单里面所有的照片
            List<HandleFile> handleFiles = Lists.newArrayList();
            response.getPictures().forEach(picture -> {
                handleFiles.add(new HandleFile(picture.getId(), picture.getName(), picture.getUrl()));
            });

            logger.info("handleFiles[{}]", handleFiles.size());

            //创建目录 压缩zip 删除 目录 保留zip
            //订单号+成品名称+数量  日期+支付订单号+size(name)+数量
            String sendName = response.getId() + "-" + DateTimeFormatter.ofPattern("yyyyMMddhhmmss")
                    .format(LocalDateTime.now()) + "-" + response.getWxNo() + "-" + response.getSizeName() + "-" + response.getNumber();

            String saveName = acceptFilePath + File.separator + sendName;

            //1.创建临时文件
            zipFileUtils.judgeFileExists(Lists.newArrayList(saveName));

            // 下载图片
            for (int i = 0; i < handleFiles.size(); i++) {

                String picPath = "";
                if (response.getFace().equals("单面")) {
                    picPath = saveName + File.separator + (i + 1) + "-" + handleFiles.get(i).getId()
                            + "-" + handleFiles.get(i).getName();
                } else {
                    picPath = saveName + File.separator + (i + 1) + "-" + handleFiles.get(i).getId()
                            + "-" + (i % 2 == 0 ? "反" : "正") + "-" + handleFiles.get(i).getName();
                }

                picList.add(picPath);
                deleteFile.add(new File(picPath));
                FileOutputStream outputStream = new FileOutputStream(picPath);
                transformHandleFile(handleFiles.get(i), outputStream);
            }

            zipFileUtils.handleErrorPicture(errorPictureAddress);

            deleteFile.add(new File(saveName));
            //第一次打包 原始包
            zip(saveName, saveName + ".zip");

            readyUploadFiles.add(new ReadyUploadFile(sendName + ".zip", response.getId(), saveName + ".zip"));
        }

        //删除原始目录
        zipFileUtils.deleteFile();
        errorPictureAddress.clear();
    }

    private void transformHandleFile(HandleFile handleFile, FileOutputStream os) throws Exception {
        URL url = new URL(handleFile.getUrl());
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        byte[] bytes = zipFileUtils.readInputStream(inputStream);
        inputStream.close();
        os.write(bytes);
        os.close();
    }

    @SneakyThrows
    public void uploadZip() {
        if (CollectionUtils.isNotEmpty(readyUploadFiles)) {
            List<UploadZipFileResponse> uploadFiles = Lists.newArrayList();
            for (ReadyUploadFile readyUploadFile : readyUploadFiles) {
                //file - > MultipartFile
                byte[] bytes = Files.readAllBytes(Paths.get(readyUploadFile.getUrl()));

                uploadFiles.add(UploadZipFileResponse.builder()
                        .orderId(readyUploadFile.getOrderId())
                        .file(new MockMultipartFile("file", readyUploadFile.getName(), "text/plain", bytes))
                        .build());
            }

            PictureServiceImpl pictureService = ApplicationContextProvider.getBean(PictureServiceImpl.class);

            //批量处理 组装数据 内存 减少时间消耗
            List<PictureResponse> uploadResponses = pictureService.upload(uploadFiles, "sourceZip");
            Map<String, String> uploadMap = uploadResponses.stream()
                    .collect(Collectors.toMap(PictureResponse::getOrderId, PictureResponse::getUrl));

            //todo 后面写入excel中进行下载

            //写入文件 进行压缩
            FileOutputStream jsonOut = new FileOutputStream(acceptFilePath + File.separator + "upload.json");
            jsonOut.write(JSON.toJSONBytes(uploadMap));
            jsonOut.close();

            //上传
            MultipartFile file = new MockMultipartFile("file", "upload.json", "text/plain",
                    Files.readAllBytes(Paths.get(acceptFilePath + File.separator + "upload.json")));

            deleteFile.add(new File(acceptFilePath + File.separator + "upload.json"));

            PictureResponse picture = pictureService.getPicture(Picture.builder()
                    .file(file)
                    .father("uploadJson")
                    .build());

            logger.info("上传结果[{}]", picture.getUrl());

            //上传完毕删除数据
            logger.info("上传完毕开始删除数据");
            zipFileUtils.deleteFile();
        }
    }

}