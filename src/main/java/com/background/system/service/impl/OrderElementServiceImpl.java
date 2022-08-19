package com.background.system.service.impl;

import com.background.system.entity.OrderElement;
import com.background.system.mapper.OrderElementMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
@Service
public class OrderElementServiceImpl {

    @Resource
    private OrderElementMapper orderElementMapper;

    
    public int deleteByPrimaryKey(Long id) {
        return orderElementMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(OrderElement record) {
        return orderElementMapper.insert(record);
    }

    
    public int insertSelective(OrderElement record) {
        return orderElementMapper.insertSelective(record);
    }

    
    public OrderElement selectByPrimaryKey(Long id) {
        return orderElementMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(OrderElement record) {
        return orderElementMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(OrderElement record) {
        return orderElementMapper.updateByPrimaryKey(record);
    }

}
