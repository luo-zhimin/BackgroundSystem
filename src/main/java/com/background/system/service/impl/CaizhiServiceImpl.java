package com.background.system.service.impl;

import com.background.system.entity.Caizhi;
import com.background.system.mapper.CaizhiMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
@Service
public class CaizhiServiceImpl {

    @Resource
    private CaizhiMapper caizhiMapper;

    
    public int deleteByPrimaryKey(Long id) {
        return caizhiMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(Caizhi record) {
        return caizhiMapper.insert(record);
    }

    
    public int insertSelective(Caizhi record) {
        return caizhiMapper.insertSelective(record);
    }

    
    public Caizhi selectByPrimaryKey(Long id) {
        return caizhiMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(Caizhi record) {
        return caizhiMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(Caizhi record) {
        return caizhiMapper.updateByPrimaryKey(record);
    }

}
