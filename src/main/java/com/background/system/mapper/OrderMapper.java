package com.background.system.mapper;

import com.background.system.entity.Orderd;
import com.background.system.response.BaseResponse;
import com.background.system.response.CurrentOrderResponse;
import com.background.system.response.IndexCountResponse;
import com.background.system.response.OrderCount;
import com.background.system.response.file.ReadyDownloadFileResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/20 14:53
 */
public interface OrderMapper extends BaseMapper<Orderd> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(Orderd record);

    Orderd selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Orderd record);

    int updateByPrimaryKey(Orderd record);

    int getCurrentOrderCount(@Param("openId")String openId);

    List<Orderd> getOrderList(@Param("page") Integer page,
                              @Param("size") Integer size,
                              @Param("openId")String openId);

    List<Orderd> getOrderAllList(@Param("page") Integer page,
                                 @Param("size") Integer size);

    int deleteOrderById(@Param("id")String orderId);

    Integer getOrderTotalMoney();

    List<OrderCount> getOrderCount();

    List<Orderd> getOrderByType(@Param("page") Integer page,
                                @Param("size") Integer size,
                                @Param("type") Integer type,
                                @Param("sizeIds")List<String> sizeIds,
                                @Param("orderId")Long orderId,
                                @Param("orderNo")String orderNo,
                                @Param("name")String name);

    int getOrderCountByType(@Param("type")Integer type,
                            @Param("sizeIds")List<String> sizeIds,
                            @Param("orderId")Long orderId,
                            @Param("orderNo")String orderNo,
                            @Param("name")String name);

    int updateKdNo(@Param("id")String id,
                   @Param("kdNo")String kdNo,
                   @Param("updateUser")String updateUser);

    int closeOrder(@Param("id")String id,
                   @Param("updateUser")String updateUser);

    List<ReadyDownloadFileResponse> getNoZipPathOrder(@Param("ids")List<String> ids);

    void updateZipPathById(@Param("baseResponse") List<BaseResponse> responses);

    int orderDownload(@Param("id")String id);

    List<IndexCountResponse> getIndexOrderCount(@Param("time")String time);

    Integer getActualMoney();

    List<Long> getCloseOrderId();

    void close(@Param("ids")List<Long> ids);

    void delete(@Param("ids")List<Long> ids);

    List<Long> getDeleteOrderIds();

    Orderd getCurrentDay();

    String getCurrentDayOrderIds();

    List<CurrentOrderResponse> getCurrentOrders(@Param("ids")List<String> ids);
}