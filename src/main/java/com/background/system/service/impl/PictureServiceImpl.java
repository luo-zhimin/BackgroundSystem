package com.background.system.service.impl;

import com.background.system.entity.Picture;
import com.background.system.mapper.PictureMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
@Service
public class PictureServiceImpl {

    @Resource
    private PictureMapper pictureMapper;

    
    public int deleteByPrimaryKey(Long id) {
        return pictureMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(Picture record) {
        return pictureMapper.insert(record);
    }

    
    public int insertSelective(Picture record) {
        return pictureMapper.insertSelective(record);
    }

    
    public Picture selectByPrimaryKey(Long id) {
        return pictureMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(Picture record) {
        return pictureMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(Picture record) {
        return pictureMapper.updateByPrimaryKey(record);
    }


    public List<Picture> getPicturesByIds(List<String> ids){
        if (CollectionUtils.isNotEmpty(ids)){
            return this.pictureMapper.getPicturesByIds(ids);
        }
        return Collections.emptyList();
    }

}
