package com.background.system.mapper;

import com.background.system.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/19 21:13
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<Goods> getGoodsList(@Param("page")Integer page,
                             @Param("size")Integer size);
}
