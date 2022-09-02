package com.background.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/8/21 15:05
 */
@Data
@ToString
public class BaseRequest {

    private String id;

    @ApiModelProperty(value = "兑换码")
    private String couponId;
}
