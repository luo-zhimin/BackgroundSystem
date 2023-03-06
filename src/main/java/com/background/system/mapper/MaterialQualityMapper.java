package com.background.system.mapper;

import com.background.system.entity.MaterialQuality;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
public interface MaterialQualityMapper extends BaseMapper<MaterialQuality> {
    int deleteByPrimaryKey(String id);

    int insert(MaterialQuality record);

    int insertSelective(MaterialQuality record);

    MaterialQuality selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MaterialQuality record);

    int updateByPrimaryKey(MaterialQuality record);

    List<MaterialQuality> getMaterialQualitiesList(@Param("page")Integer page,@Param("size")Integer size);

    int countMaterialQualities();

    List<MaterialQuality> getMaterialListByIds(@Param("ids") List<String> ids);
}