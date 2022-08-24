package com.background.system.mapper;

import com.background.system.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/20 14:53
 */
public interface OrderMapper extends BaseMapper<Order> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(Order record);

    Order selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    int getCurrentOrderCount(@Param("openId")String openId);

    List<Order> getOrderList(@Param("page") Integer page,
                             @Param("size") Integer size,
                             @Param("openId")String openId);
}