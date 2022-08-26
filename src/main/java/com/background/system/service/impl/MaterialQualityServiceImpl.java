package com.background.system.service.impl;

import com.background.system.entity.Caizhi;
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
        return caizhiMapper.getMaterialQualitiesList(null, null);
    }


    @Override
    public Caizhi getMaterialQualityDetail(Long id) {
        return selectByPrimaryKey(id);
    }

    public int deleteByPrimaryKey(Long id) {
        return caizhiMapper.deleteByPrimaryKey(id);
    }

    public int insert(Caizhi record) {
        return caizhiMapper.insert(record);
    }

    public int insertSelective(Caizhi record) {
        return caizhiMapper.insertSelective(record);
    }

    public Caizhi selectByPrimaryKey(Long id) {
        return caizhiMapper.selectByPrimaryKey(id);
    }
    
    public int updateByPrimaryKeySelective(Caizhi record) {
        return caizhiMapper.updateByPrimaryKeySelective(record);
    }
    
    public int updateByPrimaryKey(Caizhi record) {
        return caizhiMapper.updateByPrimaryKey(record);
    }
}
