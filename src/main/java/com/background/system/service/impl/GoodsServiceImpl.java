package com.background.system.service.impl;

import com.background.system.entity.Goods;
import com.background.system.entity.Picture;
import com.background.system.exception.ServiceException;
import com.background.system.mapper.GoodsMapper;
import com.background.system.response.GoodsResponse;
import com.background.system.service.GoodsService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private PictureServiceImpl pictureService;

    @Override
    public Page<GoodsResponse> getGoodsList(Integer page, Integer size) {
        log.info("getGoodsList page[{}],size[{}]", page, size);
        List<GoodsResponse> goodsResponses = new ArrayList<>();
        Page<GoodsResponse> goodsPage = new Page<>();
        goodsPage.setPages(page);
        goodsPage.setSize(size);
        page = (page - 1) * size;

        List<Goods> goodsList = goodsMapper.getGoodsList(page, size);
        int goodsCount = goodsMapper.getGoodsCount();
        if (CollectionUtils.isEmpty(goodsList)) {
            return goodsPage;
        }
        List<String> ids = Lists.newArrayList();
        //组装数据
        goodsList.forEach(goods -> {
            ids.addAll(goods.getPictureIds());
            GoodsResponse goodsResponse = new GoodsResponse();
            BeanUtils.copyProperties(goods, goodsResponse);
            goodsResponses.add(goodsResponse);
        });

        //避免长连接
        List<Picture> pictures = pictureService.getPicturesByIds(ids);

        goodsResponses.forEach(response ->
                response.setPictures(pictures.stream()
                        .filter(picture -> response.getPictureIds().contains(picture.getId() + ""))
                        .collect(Collectors.toList()))
        );

        goodsPage.setRecords(goodsResponses);
        goodsPage.setTotal(goodsCount);
        return goodsPage;
    }

    @Override
    public GoodsResponse getGoodsDetail(Long id) {
        if (id == null){
            throw new ServiceException(1000,"id不可以为空");
        }
        GoodsResponse goodsResponse = new GoodsResponse();
        Goods goods = goodsMapper.selectByPrimaryKey(id);
        if(goods!=null){
            BeanUtils.copyProperties(goods,goodsResponse);
            List<Picture> pictures = pictureService.getPicturesByIds(goods.getPictureIds());
            goodsResponse.setPictures(pictures);
        }
        return goodsResponse;
    }
}
