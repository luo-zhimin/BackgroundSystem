package com.background.system.util;

import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/17 6:42 PM
 */
public class JwtUtils {

    public static final String SECRET = "#$!@#@!$!$!@#!#@!";

    public static String getToken(String payload) {
        HashMap<String, Object> mp = new HashMap<>();
        mp.put("id", "1");
        mp.put("username", payload);
        mp.put("expire_time",System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15);
        return JWTUtil.createToken(mp, SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public static String getData(HttpServerRequest request) {
        String token = request.getHeader("token");
        JWT jwt = JWTUtil.parseToken(token);
        String id = (String) jwt.getPayload("id");
        String username = (String) jwt.getPayload("username");
        return id + "-" + username;
    }

}
