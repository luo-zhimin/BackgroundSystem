package com.background.system.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class AdminUserVO {

    @ApiModelProperty(value="主键id")
    private String id;

    @ApiModelProperty(value="名字")
    private String name;

    @ApiModelProperty(value="用户名")
    private String userName;

    @ApiModelProperty(value="密码")
    private String password;

    @ApiModelProperty(value="手机号")
    private String mobile;

    @ApiModelProperty(value="邮箱")
    private String email;

    @ApiModelProperty(value="是否删除")
    private Boolean isDelete;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime updateTime;
}
