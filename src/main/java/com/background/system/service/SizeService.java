package com.background.system.service;

import com.background.system.entity.Size;
import com.background.system.response.SizeResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/20 15:09
 */
public interface SizeService {

    Page<SizeResponse> getSizeList(Integer page, Integer size);

    SizeResponse getSizeDetail(String id);

    Boolean sizeInsert(Size size);

    Boolean sizeUpdate(Size size);

    Boolean sizeDelete(String id);

    List<SizeResponse> getSizes(Size size);
}
