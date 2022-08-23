package com.background.system.util;

import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/20 7:12 PM
 */
@Slf4j
public class Test {

    public static void main(String[] args) {
        while (true) {
            try {
                String body = HttpRequest.get("http://2.chongvip.net/poc/D202208201837151656679811.html?helo=1").header("Cookie","tokenid=sNqpTBoW8ewdr5h6Weu4sOsmh7g0lVUuTKvZGw5hkVptwX8CiytkgpRCVA2WSvw5; PHPSESSID=vmv8eg6vk98464pqfdhovprja7; SL_G_WPT_TO=zh-CN; SL_GWPT_Show_Hide_tmp=1; SL_wptGlobTipTmp=1; usertoken=11c8c0ad2a301ecc921b16efe385a83a").execute().body();
                if (body.indexOf("等待卖家发货") != -1) {
                    PushPlusUtils.push("正在充值, 快去改密码");
                }
                log.info(body);
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
