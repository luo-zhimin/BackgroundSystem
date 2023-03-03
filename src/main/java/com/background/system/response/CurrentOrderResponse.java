package com.background.system.response;

import com.background.system.annotation.Excel;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/4 01:17
 */
@Data
@ToString
@Builder
public class CurrentOrderResponse {

    @Excel(name = "序号")
    private Long id;

    @Excel(name = "下载地址")
    private String zipPath;

    @Excel(name = "省份")
    private String province;

    @Excel(name = "具体地址")
    private String address;

    @Excel(name = "联系电话")
    private String phone;

    @Excel(name = "用户姓名")
    private String name;

    @Excel(name = "订单号")
    private String orderNo;

    @Tolerate
    public CurrentOrderResponse() {}
}
