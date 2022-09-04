package com.background.system.mapper;

import com.background.system.entity.Caizhi;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
public interface CaizhiMapper extends BaseMapper<Caizhi> {
    int deleteByPrimaryKey(String id);

    int insert(Caizhi record);

    int insertSelective(Caizhi record);

    Caizhi selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Caizhi record);

    int updateByPrimaryKey(Caizhi record);

    List<Caizhi> getMaterialQualitiesList(@Param("page")Integer page,@Param("size")Integer size);

    int countMaterialQualities();

    List<Caizhi> getMaterialListByIds(@Param("ids") List<String> ids);
}