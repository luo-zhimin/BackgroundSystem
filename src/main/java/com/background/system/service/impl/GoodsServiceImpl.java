package com.background.system.service.impl;

import com.background.system.entity.Goods;
import com.background.system.mapper.GoodsMapper;
import com.background.system.service.GoodsService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/19 21:12
 */
@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public Page getGoodsList(Integer page, Integer size) {
        log.info("getGoodsList page[{}],size[{}]",page,size);
        page = (page - 1) * size;
        List<Goods> goodsList = goodsMapper.getGoodsList(page, size);
        return null;
    }
}
