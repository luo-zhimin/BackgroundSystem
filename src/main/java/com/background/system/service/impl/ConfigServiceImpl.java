package com.background.system.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.background.system.mapper.ConfigMapper;
import com.background.system.entity.Config;
import com.background.system.service.ConfigService;
@Service
public class ConfigServiceImpl implements ConfigService{

    @Resource
    private ConfigMapper configMapper;

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
