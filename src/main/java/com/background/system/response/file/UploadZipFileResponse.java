package com.background.system.response.file;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/1 17:00
 */
@Data
@ToString
@Builder
public class UploadZipFileResponse {

    private MultipartFile  file;

    private String orderId;

    @Tolerate
    public UploadZipFileResponse(){}
}
