package com.background.system.entity.token;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {

    @ApiModelProperty(value="token")
    private String token;

    @ApiModelProperty(value="过期时间")
    private long expireTime;

    @ApiModelProperty(value="用户名")
    private String username;

    @ApiModelProperty(value="密码")
    private String password;

    @ApiModelProperty(value="用户类型 小程序 1 后台2")
    private Integer type;
}
