package com.background.system.mapper;

import com.background.system.entity.Size;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
public interface SizeMapper extends BaseMapper<Size> {
    int deleteByPrimaryKey(String id);

    int insertSelective(Size record);

    Size selectByPrimaryKey(String  id);

    int updateByPrimaryKeySelective(Size record);

    int updateByPrimaryKey(Size record);

    List<Size> getSizeList(@Param("page") Integer page, @Param("size") Integer size);

    int getSizeCount();
}