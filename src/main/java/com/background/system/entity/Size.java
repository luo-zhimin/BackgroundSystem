package com.background.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
@ApiModel(value="`size`")
@Data
public class Size {
    @ApiModelProperty(value="")
    private Long id;

    @ApiModelProperty(value="尺寸")
    private String name;

    @ApiModelProperty(value="尺寸详情页的大图")
    private String pic;

    @ApiModelProperty(value="原价")
    private BigDecimal price;

    @ApiModelProperty(value="优惠后价格")
    private BigDecimal uPrice;
}