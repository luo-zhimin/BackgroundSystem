package com.background.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/19 21:12
 */
public interface GoodsService {

    Page getGoodsList(Integer page, Integer size);
}
