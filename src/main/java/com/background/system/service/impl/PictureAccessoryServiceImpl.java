package com.background.system.service.impl;

import com.background.system.entity.PictureAccessory;
import com.background.system.mapper.PictureAccessoryMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @CacheEvict(value = "pictureAccessory",key = "#id")
    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Long id) {
        return pictureAccessoryMapper.deleteByPrimaryKey(id);
    }


    @CachePut(value = "pictureAccessory",key = "#record.id")
    @Transactional(rollbackFor = Exception.class)
    public int insert(PictureAccessory record) {
        return pictureAccessoryMapper.insert(record);
    }

    @CachePut(value = "pictureAccessory",key = "#record.id")
    @Transactional(rollbackFor = Exception.class)
    public int insertSelective(PictureAccessory record) {
        return pictureAccessoryMapper.insertSelective(record);
    }

    @Cacheable(value = "pictureAccessory",key = "#id")
    public PictureAccessory selectByPrimaryKey(Long id) {
        return pictureAccessoryMapper.selectByPrimaryKey(id);
    }

    @CachePut(value = "pictureAccessory",key = "#record.id")
    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKeySelective(PictureAccessory record) {
        return pictureAccessoryMapper.updateByPrimaryKeySelective(record);
    }

    @CachePut(value = "pictureAccessory",key = "#record.id")
    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKey(PictureAccessory record) {
        return pictureAccessoryMapper.updateByPrimaryKey(record);
    }

}
