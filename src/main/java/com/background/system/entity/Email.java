package com.background.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2023/3/3 11:07
 */
@Data
@ToString
@Builder
public class Email {

    @ApiModelProperty(value = "收件人")
    private String[] addressees;

    @ApiModelProperty(value ="标题")
    private String title;

    @ApiModelProperty(value ="内容")
    private String content;

    private String attachmentName;

    private String attachmentPath;

    @Tolerate
    public Email() {}
}
