package com.background.system.service.impl;

import com.background.system.entity.Size;
import com.background.system.mapper.SizeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
@Service
public class SizeServiceImpl {

    @Resource
    private SizeMapper sizeMapper;

    
    public int deleteByPrimaryKey(Long id) {
        return sizeMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(Size record) {
        return sizeMapper.insert(record);
    }

    
    public int insertSelective(Size record) {
        return sizeMapper.insertSelective(record);
    }

    
    public Size selectByPrimaryKey(Long id) {
        return sizeMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(Size record) {
        return sizeMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(Size record) {
        return sizeMapper.updateByPrimaryKey(record);
    }

}
