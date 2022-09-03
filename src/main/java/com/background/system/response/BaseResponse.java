package com.background.system.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2022/9/3 16:06
 */
@Data
@ToString
@AllArgsConstructor
public class BaseResponse {

    private String id;

    private String name;
}
