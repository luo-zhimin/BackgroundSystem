package com.background.system.mapper;

import com.background.system.entity.Order;
import com.background.system.response.OrderCountResponse;
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

    List<Order> getOrderAllList(@Param("page") Integer page,
                                @Param("size") Integer size);

    int deleteOrderById(@Param("id")String orderId);

    List<OrderCountResponse> getOrderCount();

    int getHasKdNoCount();

    int getCloseCount();

    List<Order> getOrderByType(@Param("page") Integer page,
                               @Param("size") Integer size,
                               @Param("type") Integer type);

    int getOrderCountByType(@Param("type")Integer type);

    int updateKdNo(@Param("id")String id,@Param("kdNo")String kdNo,@Param("updateUser")String updateUser);

    int closeOrder(@Param("id")String id,@Param("updateUser")String updateUser);
}