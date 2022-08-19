package com.background.system.mapper;

import com.background.system.entity.PictureAccessory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
public interface PictureAccessoryMapper extends BaseMapper<PictureAccessory> {
    int deleteByPrimaryKey(Long id);

    int insert(PictureAccessory record);

    int insertSelective(PictureAccessory record);

    PictureAccessory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PictureAccessory record);

    int updateByPrimaryKey(PictureAccessory record);
}