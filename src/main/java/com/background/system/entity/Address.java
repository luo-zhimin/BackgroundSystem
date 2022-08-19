package com.background.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
@ApiModel(value="address")
@Data
@AllArgsConstructor
public class Address {
    @ApiModelProperty(value="")
    private Long id;

    @ApiModelProperty(value="用户唯一标识")
    private Long openid;

    @ApiModelProperty(value="姓名")
    private String name;

    @ApiModelProperty(value="手机号")
    private String phone;

    @ApiModelProperty(value="省市区")
    private String province;

    @ApiModelProperty(value="详细地址")
    private String address;

    @ApiModelProperty(value="")
    private String isDel;

    @ApiModelProperty(value="")
    private Date createTime;
}