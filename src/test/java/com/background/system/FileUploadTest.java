package com.background.system;

import com.background.system.response.file.FileUploadResponse;
import com.background.system.util.FileUploadUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2023/3/4 17:43
 */
@SpringBootTest
public class FileUploadTest {

    @Value("${file.address}")
    private String uploadAddress;

    @Value("${file.token}")
    private String token;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Test
    @SneakyThrows
    public void fileUpload(){
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "20230303101921-bd70356b49364f14bdc966c7157341c4-小卡定制-20.zip", "text/plain",
                        Files.readAllBytes(Paths.get("/Users/luozhimin/Downloads/20230303101921-bd70356b49364f14bdc966c7157341c4-小卡定制-20.zip")));
        FileUploadResponse uploadResponse = fileUploadUtil.getFileUploadResponse(mockMultipartFile, "zip",false);
        System.out.println(fileUploadUtil.upload(uploadResponse, uploadAddress, token).getData());
    }
}
