package com.background.system.controller;

import cn.hutool.jwt.JWTUtil;
import com.background.system.annotation.IgnoreLogin;
import com.background.system.entity.token.Token;
import com.background.system.entity.vo.AdminLoginVo;
import com.background.system.service.admin.IAdminUseService;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/17 6:40 PM
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "后台管理")
public class AdminController {

    @Autowired
    private IAdminUseService adminUseService;

    /**
     * 登录请求
     *
     * @param loginVo
     * @return
     */
    @IgnoreLogin
    @PostMapping("/login")
    public Result<Token> login(@RequestBody AdminLoginVo loginVo) {
        String userName = loginVo.getUsername();
        String password = loginVo.getPassword();
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            return Result.setData(200, null, "账号和密码都不能为空");
        }
        password = DigestUtils.md5Hex(password);
        Boolean loginFlag = adminUseService.queryUser(userName, password);
        if (!loginFlag) {
            return Result.setData(200, null, "账号或密码错误");
        }
        Token token = createToken(userName, password);
        return Result.success(token);
    }

    private Token createToken(String userName, String password) {
        String tokenKey = userName + password;
        long expireTime = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15;
        Map<String, Object> params = new HashMap<String, Object>(3) {
            private static final long serialVersionUID = 1L;

            {
                put("user_name", userName);
                put("password", password);
                put("expire_time", expireTime);
            }
        };
        String tokenValue = JWTUtil.createToken(params, tokenKey.getBytes());

        return Token.builder()
            .token(tokenValue)
            .expireTime(expireTime)
            .username(userName)
            .password(password)
            .build();
    }
}
