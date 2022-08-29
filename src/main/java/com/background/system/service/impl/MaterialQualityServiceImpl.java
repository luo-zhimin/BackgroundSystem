package com.background.system.service.impl;

import com.background.system.entity.Caizhi;
import com.background.system.exception.ServiceException;
import com.background.system.mapper.CaizhiMapper;
import com.background.system.service.MaterialQualityService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    private CaizhiMapper caizhiMapper;

    @Override
    public Page<Caizhi> getMaterialQualityList(Integer page, Integer size) {
        Page<Caizhi> materialPage = initPage(page, size);

        List<Caizhi> materialQualitiesList = caizhiMapper.getMaterialQualitiesList((page - 1) * size, size);
        if (CollectionUtils.isEmpty(materialQualitiesList)){
            return materialPage;
        }
        int qualities = caizhiMapper.countMaterialQualities();
        materialPage.setTotal(qualities);
        materialPage.setRecords(materialQualitiesList);
        return materialPage;
    }

    public List<Caizhi> getMaterialListByIds(List<String> ids){
        return caizhiMapper.getMaterialListByIds(ids);
    }

    @Override
    public Caizhi getMaterialQualityDetail(Long id) {
        return selectByPrimaryKey(id);
    }

    @Override
    public Boolean materialQualityInsert(Caizhi caizhi) {
        log.info("materialQuality insert [{}]",caizhi);
        return caizhiMapper.insertSelective(caizhi)>0;
    }

    @Override
    public Boolean materialQualityUpdate(Caizhi caizhi) {
        log.info("materialQuality update [{}]",caizhi);
        if (caizhi.getId()==null){
            throw new ServiceException(1003,"id不可以为空");
        }
        Caizhi live = caizhiMapper.selectByPrimaryKey(caizhi.getId());
        if (live==null){
            throw new ServiceException(1004,"该材质不存在，请确认后重新操作");
        }
        return caizhiMapper.updateByPrimaryKey(caizhi)>0;
    }

    public Caizhi selectByPrimaryKey(Long id) {
        return caizhiMapper.selectByPrimaryKey(id);
    }
}
