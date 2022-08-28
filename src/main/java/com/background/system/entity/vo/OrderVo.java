package com.background.system.entity.vo;


import com.background.system.entity.OrderElement;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;


@Data
public class OrderVo {
    @ApiModelProperty(value="主键id")
    private Long id;

    @ApiModelProperty(value="备注")
    private String remark;

    @ApiModelProperty(value="优惠券id")
    private Long couponId;

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

    @ApiModelProperty(value="详细")
    private List<OrderElement> orderElements;
}