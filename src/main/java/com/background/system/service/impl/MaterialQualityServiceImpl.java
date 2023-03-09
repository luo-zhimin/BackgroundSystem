package com.background.system.service.impl;

import com.background.system.entity.MaterialQuality;
import com.background.system.exception.ServiceException;
import com.background.system.mapper.MaterialQualityMapper;
import com.background.system.service.MaterialQualityService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
@Service
@Slf4j
public class MaterialQualityServiceImpl extends BaseService implements MaterialQualityService {

    @Resource
    private MaterialQualityMapper materialQualityMapper;

    @Override
    public Page<MaterialQuality> getMaterialQualityList(Integer page, Integer size) {
        Page<MaterialQuality> materialPage = initPage(page, size);

        List<MaterialQuality> materialQualitiesList = materialQualityMapper.getMaterialQualitiesList((page - 1) * size, size);
        if (CollectionUtils.isEmpty(materialQualitiesList)){
            return materialPage;
        }
        int qualities = materialQualityMapper.countMaterialQualities();
        materialPage.setTotal(qualities);
        materialPage.setRecords(materialQualitiesList);
        return materialPage;
    }

    @Override
    @Cacheable(value = "materialQuality")
    public List<MaterialQuality> getMaterialQualityList() {
        return materialQualityMapper.getMaterialQualitiesList(null,null);
    }

    @Override
    @Cacheable(value = "materialQuality",key = "#id")
    public MaterialQuality getMaterialQualityDetail(String id) {
        return selectByPrimaryKey(id);
    }

    @Override
    @CachePut(value = "materialQuality",key = "#materialQuality.id")
    public Boolean materialQualityInsert(MaterialQuality materialQuality) {
        log.info("materialQuality insert [{}]",materialQuality);
        if (materialQuality.getName()==null){
            throw new ServiceException(1008,"材质名字不可以为空");
        }
        return materialQualityMapper.insertSelective(materialQuality)>0;
    }

    @Override
    @CachePut(value = "materialQuality",key = "#materialQuality.id")
    public Boolean materialQualityUpdate(MaterialQuality materialQuality) {
        log.info("materialQuality update [{}]",materialQuality);
        check(materialQuality.getId());
        return materialQualityMapper.updateByPrimaryKey(materialQuality)>0;
    }

    @Override
    @Transactional
    @CacheEvict(value = "materialQuality",key = "#id")
    public Boolean deleteMaterialQuality(String id) {
        check(id);
        return materialQualityMapper.deleteByPrimaryKey(id)>0;
    }

    public MaterialQuality selectByPrimaryKey(String id) {
        return materialQualityMapper.selectByPrimaryKey(id);
    }

    private void check(String id){
        if (StringUtils.isEmpty(id)){
            throw new ServiceException(1003,"id不可以为空");
        }
        MaterialQuality live = getMaterialQualityDetail(id);
        if (live==null){
            throw new ServiceException(1004,"该材质不存在，请确认后重新操作");
        }
    }

    @Cacheable(value = "materialQuality")
    public List<MaterialQuality> getMaterialListByIds(List<String> ids){
        if (CollectionUtils.isEmpty(ids)){
            return Collections.emptyList();
        }
        return materialQualityMapper.getMaterialListByIds(ids);
    }
}
