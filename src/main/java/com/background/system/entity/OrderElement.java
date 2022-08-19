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
@ApiModel(value="order_element")
@Data
@AllArgsConstructor
public class OrderElement {
    @ApiModelProperty(value="")
    private Long id;

    @ApiModelProperty(value="订单号")
    private Long orderId;

    @ApiModelProperty(value="商品号")
    private Long goodsId;

    @ApiModelProperty(value="商品数量")
    private Integer number;

    @ApiModelProperty(value="商品图片")
    private Long picuterId;

    @ApiModelProperty(value="是否删除")
    private String isDel;

    @ApiModelProperty(value="创建时间")
    private Date createTime;
}