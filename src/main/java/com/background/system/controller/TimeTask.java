package com.background.system.controller;

import com.background.system.annotation.IgnoreLogin;
import com.background.system.mapper.OrderMapper;
import com.background.system.response.BaseResponse;
import com.background.system.response.PictureResponse;
import com.background.system.response.file.HandleFile;
import com.background.system.response.file.ReadyDownloadFileResponse;
import com.background.system.response.file.ReadyUploadFile;
import com.background.system.service.impl.OrderServiceImpl;
import com.background.system.service.impl.PictureServiceImpl;
import com.background.system.util.Result;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description:    定时任务
 * @Author: 方糖
 * @Date: 2022/9/3 4:37 PM
 */
@RestController
@RequestMapping("time")
public class TimeTask {

    private final String acceptFilePath="/Users/sugar/Desktop/BackgroundSystem";

    @Resource
    private OrderServiceImpl orderService;

    @Resource
    private PictureServiceImpl pictureService;

    @Resource
    private OrderMapper orderMapper;

    List<File> deleteFile = Lists.newArrayList();
    List<ReadyUploadFile> readyUploadFiles = Lists.newArrayList();


    @GetMapping("zipTask")
    @IgnoreLogin
    public Result<?> task () {
        deleteFile.clear();
        readyUploadFiles.clear();

        cratePictureZip();
        uploadZip();

        return Result.success("压缩上传完毕");
    }

    /**
     * 订单图片处理 压缩zip 上传服务器
     */
    @SneakyThrows
    public void cratePictureZip(){

        //扫描前创建扫描目录
        judgeFileExists(Lists.newArrayList(acceptFilePath));

        List<ReadyDownloadFileResponse> readyDownloadFIleResponses = orderService.getFile();
        if (CollectionUtils.isEmpty(readyDownloadFIleResponses)){
            System.out.println("无待处理文件");
            return;
        }

        for (ReadyDownloadFileResponse response : readyDownloadFIleResponses) {
            if (CollectionUtils.isEmpty(response.getPictures())) {
                System.out.println("该订单没有照片--" + response.getId());
                continue;
            }
            //一个订单里面所有的照片
            List<HandleFile> handleFiles = Lists.newArrayList();
            response.getPictures().forEach(picture -> {
                handleFiles.add(new HandleFile(picture.getName(), picture.getUrl()));
            });

            //创建目录 压缩zip 删除 目录 保留zip
            //订单号+成品名称+数量  日期+支付订单号+size(name)+数量
            String sendName = DateTimeFormatter.ofPattern("yyyyMMddhhmmss").format(LocalDateTime.now()) + "-" + response.getWxNo() + "-" + response.getSizeName() + "-" + response.getNumber();
            String saveName = acceptFilePath + File.separator + sendName;

            //1.创建临时文件
            judgeFileExists(Lists.newArrayList(saveName));

            //准备进行文件处理
            for (HandleFile handleFile : handleFiles) {
                FileOutputStream os = new FileOutputStream(saveName + File.separator + handleFile.getName());
                URL url = new URL(handleFile.getUrl());
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                byte[] bytes = readInputStream(inputStream);
                inputStream.close();
                os.write(bytes);
                os.close();
            }

            deleteFile.add(new File(saveName));
            //第一次打包 原始包
            zip(saveName, saveName + ".zip");

            readyUploadFiles.add(new ReadyUploadFile(sendName+".zip", response.getId(),saveName+".zip"));
        }

        //删除原始目录
        deleteFile();
    }


    @SneakyThrows
    private void uploadZip(){
        if (CollectionUtils.isNotEmpty(readyUploadFiles)){
            List<BaseResponse> baseResponses = Lists.newArrayList();
            System.out.println("开始上传zip包");
            for (ReadyUploadFile readyUploadFile : readyUploadFiles) {
                File file = new File(readyUploadFile.getUrl());
                //file - > MultipartFile
                byte[] bytes = Files.readAllBytes(Paths.get(readyUploadFile.getUrl()));
                System.out.println(readyUploadFile.getName()+" "+bytes.length);
                MockMultipartFile mockMultipartFile = new MockMultipartFile("file",readyUploadFile.getName(),"text/plain",bytes);
                PictureResponse picture = pictureService.getPicture(mockMultipartFile);
                baseResponses.add(new BaseResponse(readyUploadFile.getOrderId(),picture.getUrl()));
                deleteFile.add(new File(readyUploadFile.getUrl()));
            }
            System.out.println("上传完毕开始删除数据");
            //上传完毕删除数据
            deleteFile();

            //处理准备进入数据库
            System.out.println(baseResponses);
            orderMapper.updateZipPathById(baseResponses);
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
            System.out.println(file.getAbsolutePath());
            if (file.isDirectory()) {
                for (File listFile : Objects.requireNonNull(file.listFiles())) {
                    deleteFile(listFile);
                }
            }else if (file.isFile()){
                //file
                file.delete();
            }
            boolean delete = file.getAbsoluteFile().delete();
            System.out.println("delete " + file.getName() + " status " + delete);
        }
    }


    private void judgeFileExists(List<String> fileNames) {
        fileNames.forEach(fileName->{
            File file = new File(fileName);
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                System.out.println(file.getName()+" 第一次创建成功 "+mkdirs);
            }
        });
    }


    private Boolean zip(String inputFileName, String zipFileName) throws Exception {
        zip(zipFileName, new File(inputFileName));
        return true;
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

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[6024];
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
