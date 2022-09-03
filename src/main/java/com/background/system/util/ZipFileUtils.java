package com.background.system.util;

import com.background.system.mapper.OrderMapper;
import com.background.system.response.BaseResponse;
import com.background.system.response.PictureResponse;
import com.background.system.response.file.HandleFile;
import com.background.system.response.file.ReadyDownloadFileResponse;
import com.background.system.response.file.ReadyUploadFile;
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

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by IntelliJ IDEA.
 * 处理文件
 * @Author : 镜像
 * @create 2022/9/3 01:10
 */
@Service
public class ZipFileUtils {

    private final Logger logger = LoggerFactory.getLogger(ZipFileUtils.class);

    private String acceptFilePath = "/Users/sugar/Desktop/BackgroundSystem/upload";

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

    /**
     * 订单图片处理 压缩zip 上传服务器
     */
    @SneakyThrows
    public void cratePictureZip(){

        // init
        picList.clear();

        //扫描前创建扫描目录
        judgeFileExists(Lists.newArrayList(acceptFilePath));

        List<ReadyDownloadFileResponse> readyDownloadFIleResponses = orderService.getFile();
        if (CollectionUtils.isEmpty(readyDownloadFIleResponses)){
            logger.info("无待处理文件");
            return;
        }

        for (ReadyDownloadFileResponse response : readyDownloadFIleResponses) {
            if (CollectionUtils.isEmpty(response.getPictures())) {
                logger.info("该订单没有照片--" + response.getId());
                continue;
            }
            //一个订单里面所有的照片
            List<HandleFile> handleFiles = Lists.newArrayList();
            response.getPictures().forEach(picture -> {
                handleFiles.add(new HandleFile(picture.getName(), picture.getUrl()));
            });

            // 下载图片
            for (HandleFile handleFile : handleFiles) {
                String picPath = "/Users/sugar/Desktop/BackgroundSystem/upload/" + handleFile.getName() + ".jpg";
                picList.add(picPath);
                FileOutputStream outputStream = new FileOutputStream(picPath, true);
                URL url = new URL(handleFile.getUrl());
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                byte[] bytes = readInputStream(inputStream);
                inputStream.close();
                outputStream.write(bytes);
                outputStream.close();
            }


            //创建目录 压缩zip 删除 目录 保留zip
            //订单号+成品名称+数量  日期+支付订单号+size(name)+数量
            String sendName = DateTimeFormatter.ofPattern("yyyyMMddhhmmss").format(LocalDateTime.now()) + "-" + response.getWxNo() + "-" + response.getSizeName() + "-" + response.getNumber();

            // 获取PDF路径
            String pdfUrl = PdfUtil.imageToMergePdf(picList, sendName);
            handleFiles.add(new HandleFile(sendName + ".pdf", pdfUrl));

            String saveName = acceptFilePath + File.separator + sendName;



            System.out.println("handleFiles = " + handleFiles);


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
            deleteFile.add(new File(pdfUrl));
            //第一次打包 原始包
            zip(saveName, saveName + ".zip");

            readyUploadFiles.add(new ReadyUploadFile(sendName+".zip", response.getId(),saveName+".zip"));
        }


        //删除原始目录
        deleteFile();
    }


    @SneakyThrows
    public void uploadZip(){
        if (CollectionUtils.isNotEmpty(readyUploadFiles)){
            List<BaseResponse> baseResponses = Lists.newArrayList();
            logger.info("开始上传zip包");
            for (ReadyUploadFile readyUploadFile : readyUploadFiles) {
                //file - > MultipartFile
                byte[] bytes = Files.readAllBytes(Paths.get(readyUploadFile.getUrl()));
//                System.out.println(readyUploadFile.getName()+" "+bytes.length);
                MockMultipartFile mockMultipartFile = new MockMultipartFile("file",readyUploadFile.getName(),"text/plain",bytes);
                PictureResponse picture = pictureService.getPicture(mockMultipartFile);
                baseResponses.add(new BaseResponse(readyUploadFile.getOrderId(),picture.getUrl()));
                deleteFile.add(new File(readyUploadFile.getUrl()));
            }
            logger.info("上传完毕开始删除数据");
            //上传完毕删除数据
            deleteFile();

            //处理准备进入数据库
//            System.out.println(baseResponses);
            orderMapper.updateZipPathById(baseResponses);
            //数据清空
            baseResponses.clear();
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
            logger.info("delete " + file.getName() + " status " + delete);
        }
    }


    private void judgeFileExists(List<String> fileNames) {
        fileNames.forEach(fileName->{
            File file = new File(fileName);
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                logger.info(file.getName()+" 第一次创建成功 "+mkdirs);
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

    private byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024*1024];//10m
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