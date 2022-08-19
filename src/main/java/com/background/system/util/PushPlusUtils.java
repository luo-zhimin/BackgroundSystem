package com.background.system.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;

import java.util.HashMap;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/19 2:36 PM
 */
public class PushPlusUtils {

    public static void push(Object response) {
        HashMap<String, String> r = new HashMap<String, String>() {{
            put("channel", "wechat");
            put("content", response.toString());
            put("template", "html");
            put("title", "上传通知");
            put("token", "510188bc052847c7adb448f45ee39750");

        }};
        HttpRequest.post("https://www.pushplus.plus/api/send").body(new JSONObject(r).toString()).execute();
    }
}
