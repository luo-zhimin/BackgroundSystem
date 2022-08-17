package com.background.system.controller;

import com.background.system.entity.vo.AdminLoginVo;
import com.background.system.util.JwtUtils;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/17 6:40 PM
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "后台管理")
public class AdminController {

    @PostMapping("login")
    public Result<?> login(@RequestBody AdminLoginVo loginVo) {
        if (!"admin".equals(loginVo.getUsername()) || !"123456".equals(loginVo.getPassword())) {
            return Result.setData(200,null,"账号或密码错误");
        }
        return Result.success(JwtUtils.getToken(loginVo.getUsername()));
    }

}
