package com.background.system.util;

import com.background.system.response.ReadyDownloadFIleResponse;
import com.background.system.service.impl.OrderServiceImpl;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
 * Created by IntelliJ IDEA.
 * 处理文件
 * @Author : 镜像
 * @create 2022/9/3 01:10
 */
@SpringBootTest
public class ZipFIleUtils {

//    @Value("${zip.file}")
    private String acceptFilePath="/Users/luozhimin/Desktop/File/daily/backgroundSystem";

    @Autowired
    private OrderServiceImpl orderService;

    /**
     * 准备删除的文件
     */
    private List<File> deleteFile = Lists.newArrayList();

    private List<readyUploadFile> readyUploadFiles = Lists.newArrayList();

    /**
     * 订单图片处理 压缩zip 上传服务器
     */
    @SneakyThrows
    public void cratePictureZip(){

        //扫描前创建扫描目录
        judgeFileExists(Lists.newArrayList(acceptFilePath));

        List<ReadyDownloadFIleResponse> readyDownloadFIleResponses = orderService.getFile();
        if (CollectionUtils.isEmpty(readyDownloadFIleResponses)){
            System.out.println("无待处理文件");
            return;
        }

        for (ReadyDownloadFIleResponse response : readyDownloadFIleResponses) {
            if (CollectionUtils.isEmpty(response.getPictures())) {
                System.out.println("该订单没有照片--" + response.getId());
                continue;
            }
            //一个订单里面所有的照片
            List<HandleFile> handleFiles = Lists.newArrayList();
            response.getPictures().forEach(picture -> {
                handleFiles.add(new HandleFile(picture.getName(), picture.getUrl()));
            });

            System.out.println("handleFiles = " + handleFiles);

            //创建目录 压缩zip 删除 目录 保留zip
            //订单号+成品名称+数量  日期+支付订单号+size(name)+数量
            String sendName = DateTimeFormatter.ofPattern("yyyyMMddhhmmss").format(LocalDateTime.now()) + "-" + response.getWxNo() + "-" + response.getSizeName() + "-" + response.getNumber();
            System.out.println("sendName = " + sendName);

            String saveName = acceptFilePath + File.separator + sendName;
            System.out.println("saveName = " + saveName);

            //1.创建临时文件
            judgeFileExists(Lists.newArrayList(saveName));

            //准备进行文件处理
            for (HandleFile handleFile : handleFiles) {
                FileOutputStream os = new FileOutputStream(saveName + File.separator + handleFile.getName());
                URL url = new URL(handleFile.getUrl());
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                byte[] bytes = readInputStream(inputStream);
//                System.out.println(handleFile.getName() + "    " + bytes.length);
                inputStream.close();
                os.write(bytes);
                os.close();
            }

            deleteFile.add(new File(saveName));
            //第一次打包 原始包
            zip(saveName, saveName + ".zip");

            readyUploadFiles.add(new readyUploadFile(saveName, response.getId(),saveName+".zip"));
        }

        System.out.println("readyUploadFiles = "+readyUploadFiles);
        System.out.println();
        System.out.println("deleteFile = "+deleteFile);
        //删除原始目录
        deleteFile();
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


    @Test
    void testCreatZipOnly(){
        cratePictureZip();
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
@Data
@ToString
@AllArgsConstructor
class HandleFile{

    private String name;

    private String url;
}

@Data
@ToString
@AllArgsConstructor
class readyUploadFile{

    private String name;

    private String orderId;

    private String url;
}
