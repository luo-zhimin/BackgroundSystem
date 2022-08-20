package com.background.system.service.impl;

import com.background.system.entity.Picture;
import com.background.system.entity.Size;
import com.background.system.exception.ServiceException;
import com.background.system.mapper.SizeMapper;
import com.background.system.response.SizeResponse;
import com.background.system.service.SizeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
@Service
@Slf4j
public class SizeServiceImpl implements SizeService {

    @Resource
    private SizeMapper sizeMapper;

    @Autowired
    private PictureServiceImpl pictureService;

    @Override
    public Page<SizeResponse> getSizeList(Integer page, Integer size) {
        log.info("getGoodsList page[{}],size[{}]", page, size);
        //todo 商品-> 尺寸 下单(图片+尺寸+材质)
        List<SizeResponse> goodsResponses = new ArrayList<>();
        Page<SizeResponse> goodsPage = new Page<>();
        goodsPage.setPages(page);
        goodsPage.setSize(size);
        page = (page - 1) * size;

        List<Size> sizeList = sizeMapper.getSizeList(page, size);
        int sizeCount = sizeMapper.getSizeCount();
        if (CollectionUtils.isEmpty(sizeList)) {
            return goodsPage;
        }
        List<String> ids = Lists.newArrayList();
        //组装数据
        sizeList.forEach(goods -> {
            ids.addAll(goods.getPictureIds());
            SizeResponse sizeResponse = new SizeResponse();
            BeanUtils.copyProperties(goods, sizeResponse);
            goodsResponses.add(sizeResponse);
        });

        //避免长连接
        List<Picture> pictures = pictureService.getPicturesByIds(ids);

        goodsResponses.forEach(response ->
                response.setPictures(pictures.stream()
                        .filter(picture -> response.getPictureIds().contains(picture.getId() + ""))
                        .collect(Collectors.toList()))
        );

        goodsPage.setRecords(goodsResponses);
        goodsPage.setTotal(sizeCount);
        return goodsPage;
    }

    @Override
    public SizeResponse getSizeDetail(Long id) {
        if (id == null){
            throw new ServiceException(1000,"id不可以为空");
        }
        SizeResponse sizeResponse = new SizeResponse();
        Size size = sizeMapper.selectByPrimaryKey(id);
        if(size!=null){
            BeanUtils.copyProperties(size,sizeResponse);
            List<Picture> pictures = pictureService.getPicturesByIds(size.getPictureIds());
            sizeResponse.setPictures(pictures);
        }
        return sizeResponse;
    }


    public int deleteByPrimaryKey(Long id) {
        return sizeMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(Size record) {
        return sizeMapper.insert(record);
    }

    
    public int insertSelective(Size record) {
        return sizeMapper.insertSelective(record);
    }

    
    public Size selectByPrimaryKey(Long id) {
        return sizeMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(Size record) {
        return sizeMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(Size record) {
        return sizeMapper.updateByPrimaryKey(record);
    }

}
