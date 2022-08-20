package com.background.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/20 3:31 PM
 */
@Data
@Builder
@AllArgsConstructor
public class Coupon {

    @ApiModelProperty(value = "主键Id")
    private Long id;

    private String couponId;

    private String isUsed;

    private BigDecimal price;

    private Integer useLimit;

    private Long openId;

}
