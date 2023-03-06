package com.background.system.entity;

import com.background.system.annotation.Excel;
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
@ApiModel(value="materialQuality")
@Data
@ToString
public class MaterialQuality implements Serializable {

    private static final long serialVersionUID = -7992747140968718739L;

    @ApiModelProperty(value="主键id")
    @Excel(name = "材质编号")
    private String id;

    @ApiModelProperty(value="材质")
    @Excel(name = "材质名称")
    private String name;

    @ApiModelProperty(value="价格")
    @Excel(name = "材质价格")
    private BigDecimal price;

    private Boolean isDel;
}