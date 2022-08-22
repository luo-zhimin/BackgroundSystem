package com.background.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/20 3:31 PM
 */
@Data
//@Builder
//@AllArgsConstructor
@ToString
public class Coupon {

    @ApiModelProperty(value = "主键Id")
    private Long id;

    @ApiModelProperty(value = "兑换码")
    private String couponId;

    @ApiModelProperty(value = "是否被使用")
    private Boolean isUsed;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "消费限制")
    private Integer useLimit;

    @ApiModelProperty(value = "小程序唯一标识")
    private String openId;

    @ApiModelProperty(value = "'是否失效'")
    private Boolean status;

    private String pictureId;
}
