package com.background.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ToString
public class WechatUser implements Serializable {

    private static final long serialVersionUID = 3853684793403909772L;

    @ApiModelProperty(value="主键id")
    private Long id;

    @ApiModelProperty(value="小程序唯一标识")
    private String openId;

    @ApiModelProperty(value="开放平台唯一标识")
    private String unionId;

    @ApiModelProperty(value="用户信息")
    private String userInfo;

    @ApiModelProperty(value="是否删除")
    private String isDel;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
}