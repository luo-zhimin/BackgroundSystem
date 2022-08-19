package com.background.system.mapper;

import com.background.system.entity.Address;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
public interface AddressMapper extends BaseMapper<Address> {
    int deleteByPrimaryKey(Long id);

    int insert(Address record);

    int insertSelective(Address record);

    Address selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);
}