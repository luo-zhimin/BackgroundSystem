package com.background.system.response;

import lombok.Data;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2022/9/3 22:20
 */
@Data
@ToString
public class CountResponse {

    private String orderId;

    private Integer number;
}
