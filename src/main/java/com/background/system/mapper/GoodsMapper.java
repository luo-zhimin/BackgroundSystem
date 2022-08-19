package com.background.system.mapper;

import com.background.system.entity.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/19 21:13
 */
public interface GoodsMapper {

    List<Goods> getGoodsList(@Param("page")Integer page,
                             @Param("size")Integer size);
}
