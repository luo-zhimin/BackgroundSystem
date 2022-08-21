package com.background.system.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/17 6:41 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginVo {

    @ApiModelProperty(value="用户名")
    private String username;

    @ApiModelProperty(value="密码")
    private String password;
}
