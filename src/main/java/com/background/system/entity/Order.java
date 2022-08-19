package com.background.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
@ApiModel(value="`order`")
@Data
@AllArgsConstructor
public class Order {
    @ApiModelProperty(value="")
    private Long id;

    @ApiModelProperty(value="微信支付订单号")
    private String orderNo;

    @ApiModelProperty(value="快递单号")
    private String kdNo;

    @ApiModelProperty(value="是否支付")
    private String isPay;

    @ApiModelProperty(value="省市区")
    private String province;

    @ApiModelProperty(value="详细地址")
    private String address;

    @ApiModelProperty(value="备注")
    private String remark;

    @ApiModelProperty(value="联系人姓名")
    private String contactName;

    @ApiModelProperty(value="联系人手机号")
    private String contactPhone;

    @ApiModelProperty(value="运费")
    private BigDecimal portPrice;

    @ApiModelProperty(value="是否删除")
    private String isDel;

    @ApiModelProperty(value="创建时间")
    private Date createTime;

    @ApiModelProperty(value="修改时间")
    private Date updateTime;

    @ApiModelProperty(value="商品总价（不包含优惠券）")
    private BigDecimal total;

    @ApiModelProperty(value="优惠券id")
    private Long couponId;

    @ApiModelProperty(value="订单状态：待付款，待发货，售后订单，交易关闭")
    private String status;
}