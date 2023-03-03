package com.background.system.service.impl;

import com.background.system.cache.ConfigCache;
import com.background.system.entity.Config;
import com.background.system.mapper.ConfigMapper;
import com.background.system.request.ConfigRequest;
import com.background.system.service.ConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
public class ConfigServiceImpl extends BaseService implements ConfigService{

    @Resource
    private ConfigMapper configMapper;

    @Override
    @Cacheable(value = "config",key = "#id")
    public Config selectByPrimaryKey(Long id) {
        return configMapper.selectById(id);
    }

    @Override
    public Page<Config> getConfigList(Integer page, Integer size) {
        Page<Config> configPage = initPage(page, size);
        Long count = configMapper.selectCount(new QueryWrapper<>());
        List<Config> configs = configMapper.getConfigs();
        configPage.setTotal(count);
        configPage.setRecords(configs);
        return configPage;
    }

    @Override
    @Transactional
    @CachePut(value = "config",key = "#request.id")
    public Boolean addConfig(ConfigRequest request) {
        configMapper.insert(request);
        ConfigCache.configMap.put(request.getConfigKey(), request.getConfigValue());
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    @CachePut(value = "config",key = "#request.id")
    public Boolean updateConfig(ConfigRequest request) {
        configMapper.updateById(request);
        ConfigCache.configMap.put(request.getConfigKey(), request.getConfigValue());
        return Boolean.TRUE;
    }

    public ConcurrentMap<String,String> getConfigs(){
        List<Config> configs = configMapper.getConfigs();
        return configs.stream().collect(Collectors.toConcurrentMap(Config::getConfigKey, Config::getConfigValue));
    }

    @CachePut(value = "config",key = "#key",unless = "#result == null")
    public String getConfig(String key){
        return configMapper.selectOne(new QueryWrapper<Config>().eq("config_key",key)).getConfigValue();
    }

}
