package com.background.system.response.file;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/4 17:34
 */
@Data
@ToString
@Builder
public class FileUploadResponse {

    private String fileName;

    private String contentType;

    private InputStream in;

    private String dir;

    @ApiModelProperty(value = "文件上传增加rename参数,rename=false时,上传的文件名将不加上uuid,必须要保证上传文件名唯一")
    private Boolean rename;

    @Tolerate
    public FileUploadResponse() {}
}
