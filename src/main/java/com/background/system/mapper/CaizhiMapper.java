package com.background.system.mapper;

import com.background.system.entity.Caizhi;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
public interface CaizhiMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Caizhi record);

    int insertSelective(Caizhi record);

    Caizhi selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Caizhi record);

    int updateByPrimaryKey(Caizhi record);
}