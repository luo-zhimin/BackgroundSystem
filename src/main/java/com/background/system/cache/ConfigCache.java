package com.background.system.cache;

import com.background.system.service.impl.ConfigServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/8/24 21:03
 */
@Component
@Slf4j
public class ConfigCache {

    public static ConcurrentMap<String,String> configMap = new ConcurrentHashMap<>();

    @Autowired
    private ConfigServiceImpl configService;

    @PostConstruct
    public void initConfigCache(){
        configMap = configService.getConfigs();
        //todo config 页面更新 更新二级缓冲或者重新加载
        log.info("load cache mapSize[{}]", configMap.size());
    }
}
