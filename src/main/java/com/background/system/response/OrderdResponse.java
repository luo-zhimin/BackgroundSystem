package com.background.system.response;

import com.background.system.entity.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/8/23 16:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class OrderdResponse extends Orderd {

    private List<OrderElementResponse> elements;

    private Address address;

    private Coupon coupon;

    private List<Caizhi> materialQualities;

    private Size size;

    @ApiModelProperty(value="购买数量")
    private Integer num;

    @Tolerate
    public OrderdResponse(){}
}
