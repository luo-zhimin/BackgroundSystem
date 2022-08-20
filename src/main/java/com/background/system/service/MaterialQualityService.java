package com.background.system.service;

import com.background.system.entity.Caizhi;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * Created by IntelliJ IDEA.
 * @Author : 志敏.罗
 * @create 2022/8/20 15:44
 */
public interface MaterialQualityService {

    Page<Caizhi> getMaterialQualityList(Integer page, Integer size);

    Caizhi getMaterialQualityDetail(Long id);
}