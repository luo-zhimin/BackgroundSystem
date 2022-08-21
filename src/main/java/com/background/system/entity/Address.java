package com.background.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
@ApiModel(value="address")
@Data
@EqualsAndHashCode
//@AllArgsConstructor
public class Address {

    @ApiModelProperty(value="主键id")
    private String id;

    @ApiModelProperty(value="用户唯一标识")
    private String openid;

    @ApiModelProperty(value="姓名")
    private String name;

    @ApiModelProperty(value="手机号")
    private String phone;

    @ApiModelProperty(value="省市区")
    private String province;

    @ApiModelProperty(value="详细地址")
    private String address;

    @ApiModelProperty(value="是否是默认地址")
    private Boolean isDefault;

    @ApiModelProperty(value="是否删除")
    private Boolean isDel;

    @ApiModelProperty(value="创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createTime;
}