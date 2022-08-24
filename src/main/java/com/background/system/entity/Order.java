package com.background.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
@ApiModel(value="`order`")
@Data
public class Order {
    @ApiModelProperty(value="主键Id")
    private Long id;

    @ApiModelProperty(value="微信支付订单号")
    private String wxNo;

    @ApiModelProperty(value="快递单号")
    private String kdNo;

    @ApiModelProperty(value="是否支付")
    private Boolean isPay;

    @ApiModelProperty(value="备注")
    private String remark;

    @ApiModelProperty(value="运费")
    private BigDecimal portPrice;

    @ApiModelProperty(value="是否删除")
    private Boolean isDel;

    @ApiModelProperty(value="创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createTime;

    @ApiModelProperty(value="修改时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime updateTime;

    @ApiModelProperty(value="商品总价（不包含优惠券）")
    private BigDecimal total;

    @ApiModelProperty(value="优惠券id")
    private Long couponId;

    @ApiModelProperty(value="订单状态：待付款，待发货，售后订单，交易关闭")
    private String status;

    @ApiModelProperty(value="收货地址id")
    private Long addressId;

    @ApiModelProperty(value="购买数量")
    private Integer num;

    @ApiModelProperty(value="下单图片id，逗号分割")
    private String pictureId;

    @ApiModelProperty(value="尺寸ID")
    private Long sizeId;

    @ApiModelProperty(value="材质ID")
    private Long caizhiId;

    private String createUser;

    public List<String> getPictureIds() {
        if (StringUtils.isNotBlank(this.pictureId)) {
            return Lists.newArrayList(this.pictureId.split(","));
        }
        return Collections.emptyList();
    }

}