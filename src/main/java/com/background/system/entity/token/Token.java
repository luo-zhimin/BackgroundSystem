package com.background.system.entity.token;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {

    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    private long expireTime;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
