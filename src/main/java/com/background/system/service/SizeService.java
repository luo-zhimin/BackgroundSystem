package com.background.system.service;

import com.background.system.response.SizeResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/20 15:09
 */
public interface SizeService {

    Page<SizeResponse> getSizeList(Integer page, Integer size);

    SizeResponse getSizeDetail(String id);
}
