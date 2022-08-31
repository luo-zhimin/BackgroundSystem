package com.background.system.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2022/8/31 15:09
 */
@Data
@ToString
@Builder
public class PictureResponse {

    private String id;

    private String url;

    @Tolerate
    public PictureResponse(){}
}
