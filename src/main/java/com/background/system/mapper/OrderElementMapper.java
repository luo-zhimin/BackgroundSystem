package com.background.system.mapper;

import com.background.system.entity.Order;
import com.background.system.entity.OrderElement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
public interface OrderElementMapper extends BaseMapper<OrderElement> {
    int deleteByPrimaryKey(Long id);

    int insert(OrderElement record);

    int insertSelective(OrderElement record);

    OrderElement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderElement record);

    int updateByPrimaryKey(OrderElement record);
}