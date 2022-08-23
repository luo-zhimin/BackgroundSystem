package com.background.system.response;

import com.background.system.entity.*;
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
public class OrderResponse extends Order {

    private List<Picture> pictures;

    private Address address;

    private Coupon coupon;

    private Caizhi caizhi;

    private Size size;

    @Tolerate
    public OrderResponse(){}
}
