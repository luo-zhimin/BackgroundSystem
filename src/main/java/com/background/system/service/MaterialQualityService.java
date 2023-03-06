package com.background.system.service;

import com.background.system.entity.MaterialQuality;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * @Author : 志敏.罗
 * @create 2022/8/20 15:44
 */
public interface MaterialQualityService {

    Page<MaterialQuality> getMaterialQualityList(Integer page, Integer size);

    List<MaterialQuality> getMaterialQualityList();

    MaterialQuality getMaterialQualityDetail(String id);

    Boolean materialQualityInsert(MaterialQuality materialQuality);

    Boolean materialQualityUpdate(MaterialQuality materialQuality);

    Boolean deleteMaterialQuality(String id);
}
