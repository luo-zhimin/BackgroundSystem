package com.background.system.response;

import lombok.Data;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2022/8/31 21:10
 */
@Data
@ToString
public class OrderCountResponse {

    private Integer totalCount;

    private Integer payCount;

    private Boolean isPay;

    private Integer delCount;

    private Boolean isDel;
}
