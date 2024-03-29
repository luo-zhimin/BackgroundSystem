package com.background.system.entity;

import com.background.system.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
@ApiModel(value="picture")
@Data
@AllArgsConstructor
@Builder
public class Picture implements Serializable {

    private static final long serialVersionUID = 5845772495741906838L;

    private MultipartFile file;

    @ApiModelProperty(value="主键Id")
    @Excel(name = "图片编号")
    private String id;

    @ApiModelProperty(value="文件名字")
    @Excel(name = "图片名字")
    private String name;

    @ApiModelProperty(value="图片地址")
    @Excel(name = "图片下载地址")
    private String url;

    @ApiModelProperty(value="是否删除")
    private Boolean isDel;

    @ApiModelProperty(value = "父级目录")
    private String father;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createTime;

    @Tolerate
    public Picture(){}
}