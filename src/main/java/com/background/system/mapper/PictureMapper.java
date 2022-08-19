package com.background.system.mapper;

import com.background.system.entity.Picture;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
public interface PictureMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Picture record);

    int insertSelective(Picture record);

    Picture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Picture record);

    int updateByPrimaryKey(Picture record);

    List<Picture> getPicturesByIds(@Param("ids")List<String> ids);
}