package com.background.system.service.impl;

import com.background.system.entity.PictureAccessory;
import com.background.system.mapper.PictureAccessoryMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
@Service
public class PictureAccessoryServiceImpl {

    @Resource
    private PictureAccessoryMapper pictureAccessoryMapper;

    
    public int deleteByPrimaryKey(Long id) {
        return pictureAccessoryMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(PictureAccessory record) {
        return pictureAccessoryMapper.insert(record);
    }

    
    public int insertSelective(PictureAccessory record) {
        return pictureAccessoryMapper.insertSelective(record);
    }

    
    public PictureAccessory selectByPrimaryKey(Long id) {
        return pictureAccessoryMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(PictureAccessory record) {
        return pictureAccessoryMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(PictureAccessory record) {
        return pictureAccessoryMapper.updateByPrimaryKey(record);
    }

}
