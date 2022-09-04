package com.background.system.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/9/4 10:24
 */
@Data
@ToString
@ApiModel(description = "首页对象")
public class IndexCountResponse {

    private Integer time;

    private Integer count;
}
