package com.background.system.cache;

import com.alibaba.fastjson.JSON;
import com.background.system.service.impl.ConfigServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/8/24 21:03
 */
@Component
@Slf4j
public class ConfigCache implements CommandLineRunner {

    public static ConcurrentHashMap<String, String> configMap = new ConcurrentHashMap<>();

    @Autowired
    private ConfigServiceImpl configService;

    @Override
    public void run(String... args) throws Exception {
        configMap = configService.getConfigs();
        //todo config 页面更新 更新二级缓冲或者重新加载
        log.info("runner cache map[{}]", JSON.toJSONString(configMap));
    }
}
