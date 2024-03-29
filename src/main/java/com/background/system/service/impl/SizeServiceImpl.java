package com.background.system.service.impl;

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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
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
        log.info("getSizeList page[{}],size[{}]", page, size);
        //todo 商品-> 尺寸 下单(图片+尺寸+材质)
        Page<SizeResponse> sizePage = initPage(page, size);

        page = (page - 1) * size;

        List<Size> sizeList = sizeMapper.getSizeList(page, size);
        Long sizeCount = sizeMapper.selectCount(new QueryWrapper<Size>()
                .eq("is_del", false));

        if (CollectionUtils.isEmpty(sizeList)) {
            return sizePage;
        }

        //封装返回数据
        List<SizeResponse> sizeResponses = transSize(sizeList);

        sizePage.setRecords(sizeResponses);
        sizePage.setTotal(sizeCount);
        return sizePage;
    }

    @Override
    @Cacheable(value = "size",key = "#id",unless = "#result == null")
    public SizeResponse getSizeDetail(String id) {
        if (id == null){
            throw new ServiceException(1000,"id不可以为空");
        }
        SizeResponse sizeResponse = new SizeResponse();
        Size size = sizeMapper.selectByPrimaryKey(id);
        if(size!=null){
            BeanUtils.copyProperties(size,sizeResponse);
            sizeResponse.setPictures(pictureService.getPicturesByIds(size.getPictureIds()));
            sizeResponse.setMaterials(materialQualityService.getMaterialListByIds(size.getMaterialIds()));
            sizeResponse.setDetailPictures(pictureService.getPicturesByIds(size.getDetailPictureIds()));
        }
        return sizeResponse;
    }

    @Override
    @Transactional
    @CachePut(value = "size",key = "#size.id")
    public Boolean sizeInsert(Size size) {
        log.info("size insert [{}]",size);
        if (CollectionUtils.isEmpty(size.getMaterialIds()) && (size.getFaces()==null || size.getSize()==null)){
            throw new ServiceException(1029,"请确认后在操作");
        }
        if (StringUtils.isNotEmpty(size.getSize()) && !size.getSize().contains("*")) {
            throw new ServiceException(1003, "尺寸填写错误，请按照正常格式来填写 宽*高");
        }
        return sizeMapper.insertSelective(size)>0;
    }

    @Override
    @Transactional
    @CachePut(value = "size",key = "#size.id")
    public Boolean sizeUpdate(Size size) {
        log.info("size update [{}]",size);
        checkSize(size.getId());
        return sizeMapper.updateByPrimaryKeySelective(size)>0;
    }

    @Override
    @Transactional
    @CacheEvict(value = "size",key = "#id")
    public Boolean sizeDelete(String id) {
        log.info("size delete [{}]",id);
        checkSize(id);
        return sizeMapper.deleteByPrimaryKey(id)>0;
    }

    @Override
    public List<SizeResponse> getSizes(Size size) {
        List<Size> sizes = sizeMapper.selectList(new QueryWrapper<>(size)
                .eq("is_del",false));
        if (CollectionUtils.isEmpty(sizes)){
            return Collections.emptyList();
        }
        return transSize(sizes);
    }

    private void checkSize(String id){
        if (StringUtils.isEmpty(id)){
            throw new ServiceException(1003,"id不可以为空");
        }
        Size live = sizeMapper.selectByPrimaryKey(id);
        if (live==null){
            throw new ServiceException(1004,"该尺寸不存在，请确认后重新操作");
        }
    }

    private List<SizeResponse> transSize(List<Size> sizes){
        List<SizeResponse> sizeResponses = new ArrayList<>();
        List<String> ids = Lists.newArrayList();
        //组装数据
        sizes.forEach(sz -> {
            ids.addAll(sz.getPictureIds());
            ids.addAll(sz.getDetailPictureIds());
            SizeResponse sizeResponse = new SizeResponse();
            BeanUtils.copyProperties(sz, sizeResponse);
            sizeResponses.add(sizeResponse);
        });

        //避免长连接
        List<Picture> pictures = pictureService.getPicturesByIds(ids);

        sizeResponses.forEach(response -> {
            response.setPictures(pictures.stream()
                    .filter(picture -> response.getPictureIds().contains(picture.getId()))
                    .collect(Collectors.toList()));
            response.setDetailPictures(pictures.stream()
                    .filter(picture -> response.getDetailPictureIds().contains(picture.getId()))
                    .collect(Collectors.toList()));
        });
        return sizeResponses;
    }
}
