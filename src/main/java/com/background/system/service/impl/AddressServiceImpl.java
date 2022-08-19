package com.background.system.service.impl;

import com.background.system.entity.Address;
import com.background.system.mapper.AddressMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
@Service
public class AddressServiceImpl {

    @Resource
    private AddressMapper addressMapper;

    
    public int deleteByPrimaryKey(Long id) {
        return addressMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(Address record) {
        return addressMapper.insert(record);
    }

    
    public int insertSelective(Address record) {
        return addressMapper.insertSelective(record);
    }

    
    public Address selectByPrimaryKey(Long id) {
        return addressMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(Address record) {
        return addressMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(Address record) {
        return addressMapper.updateByPrimaryKey(record);
    }

}
