package com.background.system.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminUserVO {

    private String id;
    private String name;
    private String userName;
    private String password;
    private String mobile;
    private String email;
    private Boolean isDelete;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
