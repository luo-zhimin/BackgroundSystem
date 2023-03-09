package com.background.system.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2022/8/31 15:09
 */
@Data
@ToString
@Builder
public class PictureResponse implements Serializable {

    private static final long serialVersionUID = -8992410913157215639L;

    private String id;

    private String url;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderId;

    @Tolerate
    public PictureResponse(){}
}
