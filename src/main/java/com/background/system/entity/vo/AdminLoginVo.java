package com.background.system.entity.vo;

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
    private String username;
    private String password;
}
