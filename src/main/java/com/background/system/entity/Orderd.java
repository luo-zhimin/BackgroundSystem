package com.background.system.entity;

import com.background.system.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
@ApiModel(description="`order`")
@Data
public class Orderd implements Serializable {

    private static final long serialVersionUID = 1525223375313707081L;

    @ApiModelProperty(value="主键Id")
    private String id;

    @ApiModelProperty(value="微信支付订单号")
    @TableField(value = "order_no")
    @Excel(name = "微信支付订单号")
    private String wxNo;

    @ApiModelProperty(value="快递单号")
    @Excel(name = "快递单号")
    private String kdNo;

    @ApiModelProperty(value="是否支付")
    @Excel(name = "是否支付", readConverterExp = "true=否,false=是")
    private Boolean isPay;

    @ApiModelProperty(value="备注")
    @Excel(name = "备注")
    private String remark;

    @ApiModelProperty(value="运费")
    @Excel(name = "运费")
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
    @Excel(name = "商品总价（不包含优惠券）")
    private BigDecimal total;

    @ApiModelProperty(value="优惠券id")
    private Long couponId;

    @ApiModelProperty(value="订单状态：待付款，待发货，售后订单，交易关闭")
    @Excel(name = "付款状态", readConverterExp = "0=待付款,1=待发货,2=配送中,3=已完成,4=已取消")
    private String status;

    @ApiModelProperty(value="收货地址id")
    private String addressId;

    @ApiModelProperty(value="尺寸ID")
    private String sizeId;

    @ApiModelProperty(value="材质ID")
    private String caizhiId;

    private String createUser;

    private String updateUser;

    @Excel(name = "下载地址")
    private String zipPath;

    private Boolean isDownload;

    private Integer number;

    public List<String> getMaterialQualityIds(){
        if (StringUtils.isNotEmpty(this.caizhiId)){
            return Lists.newArrayList(caizhiId.split(","));
        }
        return Collections.emptyList();
    }
}