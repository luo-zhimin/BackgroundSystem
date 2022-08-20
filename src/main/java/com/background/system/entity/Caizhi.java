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
@ApiModel(value="caizhi")
@Data
public class Caizhi {
    @ApiModelProperty(value="")
    private Long id;

    @ApiModelProperty(value="材质")
    private String name;

    @ApiModelProperty(value="价格")
    private BigDecimal price;
}