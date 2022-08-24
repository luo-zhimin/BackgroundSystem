package com.background.system.service.impl;

import com.background.system.entity.Config;
import com.background.system.mapper.ConfigMapper;
import com.background.system.service.ConfigService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ConfigServiceImpl implements ConfigService{

    @Resource
    private ConfigMapper configMapper;

    public List<Config> getConfigsByKeys(List<String> keys){
        return Optional.ofNullable(configMapper.getConfigsByKeys(keys)).orElse(Lists.newArrayList());
    }

    public ConcurrentHashMap<String,String> getConfigs(){
        List<Config> configs = configMapper.getConfigs();
        return (ConcurrentHashMap<String, String>) configs.stream().collect(Collectors.toConcurrentMap(Config::getConfigKey, Config::getConfigValue));
    }


    @Override
    public int deleteByPrimaryKey(Integer id) {
        return configMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Config record) {
        return configMapper.insert(record);
    }

    @Override
    public int insertSelective(Config record) {
        return configMapper.insertSelective(record);
    }

    @Override
    public Config selectByPrimaryKey(Integer id) {
        return configMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Config record) {
        return configMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Config record) {
        return configMapper.updateByPrimaryKey(record);
    }

}
