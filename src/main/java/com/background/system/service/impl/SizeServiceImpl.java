package com.background.system.service.impl;

import com.background.system.entity.Caizhi;
import com.background.system.entity.Picture;
import com.background.system.entity.Size;
import com.background.system.exception.ServiceException;
import com.background.system.mapper.SizeMapper;
import com.background.system.response.SizeResponse;
import com.background.system.service.SizeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class SizeServiceImpl extends BaseService implements SizeService {

    @Resource
    private SizeMapper sizeMapper;

    @Autowired
    private PictureServiceImpl pictureService;

    @Autowired
    private MaterialQualityServiceImpl materialQualityService;

    @Override
    public Page<SizeResponse> getSizeList(Integer page, Integer size) {
        log.info("getGoodsList page[{}],size[{}]", page, size);
        //todo 商品-> 尺寸 下单(图片+尺寸+材质)
        List<SizeResponse> goodsResponses = new ArrayList<>();
        Page<SizeResponse> sizePage = initPage(page, size);

        page = (page - 1) * size;

        List<Size> sizeList = sizeMapper.getSizeList(page, size);
        Long sizeCount = sizeMapper.selectCount(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(sizeList)) {
            return sizePage;
        }
        List<String> ids = Lists.newArrayList();
//        List<String> materialIds = Lists.newArrayList();
        //组装数据
        sizeList.forEach(goods -> {
            ids.addAll(goods.getPictureIds());
//            materialIds.addAll(goods.getMaterialIds());
            SizeResponse sizeResponse = new SizeResponse();
            BeanUtils.copyProperties(goods, sizeResponse);
            goodsResponses.add(sizeResponse);
        });

        //避免长连接
        List<Picture> pictures = pictureService.getPicturesByIds(ids);
//        List<Caizhi> materialLists = materialQualityService.getMaterialListByIds(materialIds);

        goodsResponses.forEach(response -> {
            response.setPictures(pictures.stream()
                    .filter(picture -> response.getPictureIds().contains(picture.getId() + ""))
                    .collect(Collectors.toList()));
//            response.setMaterials(materialLists.stream()
//                    .filter(material -> response.getMaterialIds().contains(material.getId()+""))
//                    .collect(Collectors.toList()));
        });

        sizePage.setRecords(goodsResponses);
        sizePage.setTotal(sizeCount);
        return sizePage;
    }

    @Override
    public SizeResponse getSizeDetail(String id) {
        if (id == null){
            throw new ServiceException(1000,"id不可以为空");
        }
        SizeResponse sizeResponse = new SizeResponse();
        Size size = sizeMapper.selectByPrimaryKey(Long.parseLong(id));
        if(size!=null){
            BeanUtils.copyProperties(size,sizeResponse);
            List<Picture> pictures = pictureService.getPicturesByIds(size.getPictureIds());
            sizeResponse.setPictures(pictures);
            List<Caizhi> materials = materialQualityService.getMaterialListByIds(size.getMaterialIds());
            sizeResponse.setMaterials(materials);
        }
        return sizeResponse;
    }

}
