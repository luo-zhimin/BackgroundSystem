package com.background.system.mapper;

import com.background.system.entity.Size;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
public interface SizeMapper extends BaseMapper<Size> {
    int deleteByPrimaryKey(Long id);

    int insert(Size record);

    int insertSelective(Size record);

    Size selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Size record);

    int updateByPrimaryKey(Size record);
}