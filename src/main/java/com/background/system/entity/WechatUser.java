package com.background.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@ToString
public class WechatUser {

    private Long id;

    /**
     * open Id
     */
    private String openId;

    private String unionId;

    /**
     * 用户信息
     */
    private String userInfo;

    /**
     * 是否删除
     */
    private String isDel;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createTime;
}