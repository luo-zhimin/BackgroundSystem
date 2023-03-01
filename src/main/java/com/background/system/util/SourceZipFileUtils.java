package com.background.system.util;

import com.alibaba.fastjson.JSON;
import com.background.system.mapper.OrderMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by IntelliJ IDEA.
 * 处理文件
 *
 * @Author : 镜像
 * @create 2022/9/3 01:10
 */
@Service
@SuppressWarnings({"all"})
public class SourceZipFileUtils {

    private final Logger logger = LoggerFactory.getLogger(SourceZipFileUtils.class);

    @Value("${zip.path}")
    private String acceptFilePath;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private PictureServiceImpl pictureService;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 准备删除的文件
     */
    public List<File> deleteFile = Lists.newArrayList();

    public List<String> picList = new ArrayList<>();

    public List<ReadyUploadFile> readyUploadFiles = Lists.newArrayList();

    public static List<String> errorPictureAddress = new ArrayList<>();

    /**
     * 订单图片处理 压缩zip 上传服务器
     */
    @SneakyThrows
    public void cratePictureZip() {

        // init
        picList.clear();

        //扫描前创建扫描目录
        judgeFileExists(Lists.newArrayList(acceptFilePath));

        List<ReadyDownloadFileResponse> readyDownloadFIleResponses = orderService.getFile();
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
            judgeFileExists(Lists.newArrayList(saveName));

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
                FileOutputStream outputStream = new FileOutputStream(picPath);
                transformHandleFile(handleFiles.get(i), outputStream);
            }

            if (CollectionUtils.isNotEmpty(errorPictureAddress)) {
                logger.info("error picture write [{}]", JSON.toJSONString(errorPictureAddress));
                FileOutputStream jsonOut = new FileOutputStream(saveName + File.separator + sendName + ".json");
                jsonOut.write(JSON.toJSONBytes(errorPictureAddress));
                jsonOut.close();
            }

            deleteFile.add(new File(saveName));
            //第一次打包 原始包
            zip(saveName, saveName + ".zip");

            readyUploadFiles.add(new ReadyUploadFile(sendName + ".zip", response.getId(), saveName + ".zip"));
        }

        //删除原始目录
        deleteFile();
        errorPictureAddress.clear();
    }

    private void transformHandleFile(HandleFile handleFile, FileOutputStream os) throws Exception {
        URL url = new URL(handleFile.getUrl());
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        byte[] bytes = readInputStream(inputStream);
        inputStream.close();
        os.write(bytes);
        os.close();
    }


    @SneakyThrows
    public void uploadZip() {
        if (CollectionUtils.isNotEmpty(readyUploadFiles)) {
            List<UploadZipFileResponse> uploadFiles = Lists.newArrayList();
            for (int i = 0; i < readyUploadFiles.size(); i++) {
                //file - > MultipartFile
                byte[] bytes = Files.readAllBytes(Paths.get(readyUploadFiles.get(i).getUrl()));

                uploadFiles.add(UploadZipFileResponse.builder()
                        .orderId(readyUploadFiles.get(i).getOrderId())
                        .file(new MockMultipartFile("file", readyUploadFiles.get(i).getName(), "text/plain", bytes))
                        .build());
            }

            //批量处理 组装数据 内存 减少时间消耗
            List<PictureResponse> uploadResponses = pictureService.upload(uploadFiles, "sourceZip");
            Map<String, String> uploadMap = uploadResponses.stream()
                    .collect(Collectors.toMap(PictureResponse::getOrderId, PictureResponse::getUrl));

            //写入文件 进行压缩
            FileOutputStream jsonOut = new FileOutputStream(acceptFilePath + File.separator + "upload.json");
            jsonOut.write(JSON.toJSONBytes(uploadMap));
            jsonOut.close();
            //压缩
            zip(acceptFilePath, acceptFilePath + File.separator + "upload.zip");

            //上传
            MultipartFile file = new MockMultipartFile("file", "upload.zip", "text/plain",
                    Files.readAllBytes(Paths.get(acceptFilePath + File.separator + "upload.zip")));

            PictureResponse picture = pictureService.getPicture(file, "uploadZip");

            logger.info("上传结果[{}]", picture.getUrl());

            //上传完毕删除数据
            logger.info("上传完毕开始删除数据");
            deleteFile();
        }
    }


    private void deleteFile() {
        deleteFile.forEach(this::deleteFile);
    }

    private void deleteFile(File file) {
        if (file.exists()) {
            if (file.getAbsolutePath().equals(acceptFilePath)) {
                return;
            }
            if (file.isDirectory()) {
                for (File listFile : Objects.requireNonNull(file.listFiles())) {
                    deleteFile(listFile);
                }
            } else if (file.isFile()) {
                //file
                file.delete();
            }
            boolean delete = file.getAbsoluteFile().delete();
            logger.info("delete " + file.getName() + " status " + delete);
        }
    }


    private void judgeFileExists(List<String> fileNames) {
        fileNames.forEach(fileName -> {
            File file = new File(fileName);
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                logger.info(file.getName() + " 第一次创建成功 " + mkdirs);
            }
        });
    }


    private void zip(String inputFileName, String zipFileName) throws Exception {
        zip(zipFileName, new File(inputFileName));
    }

    private void zip(String zipFileName, File inputFile) throws Exception {
        ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFileName)));
        zip(out, inputFile, "");
        out.flush();
        out.close();
    }

    private void zip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            assert fl != null;
            for (File file : fl) {
                zip(out, file, base + file.getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }
    }

    private byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024 * 1024];//10m
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }
}