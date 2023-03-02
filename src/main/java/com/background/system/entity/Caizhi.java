package com.background.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
@ApiModel(value="caizhi")
@Data
@ToString
public class Caizhi implements Serializable {

    private static final long serialVersionUID = -7992747140968718739L;

    @ApiModelProperty(value="主键id")
    private String id;

    @ApiModelProperty(value="材质")
    private String name;

    @ApiModelProperty(value="价格")
    private BigDecimal price;

    private Boolean isDel;
}