package com.background.system.mapper;

import com.background.system.entity.OrderElement;
import com.background.system.response.CountResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2022/8/27 13:56
 */
public interface OrderElementsMapper {

    void batchInsert(@Param("elements")List<OrderElement> elements);

    List<OrderElement> getOrderElementsByOrderId(@Param("orderId")String orderId);

    List<CountResponse> getOrderCountByIds(@Param("ids")List<String> ids);
}
