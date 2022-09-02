package com.background.system.mapper;

import com.background.system.entity.Address;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
public interface AddressMapper extends BaseMapper<Address> {

    int deleteByPrimaryKey(Long id);

    int insertSelective(Address record);

    Address selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);


    int deleteAddressById(@Param("id")String id);

    List<Address> getAddressList(@Param("page")Integer page,
                                 @Param("size")Integer size,
                                 @Param("openId")String openId);

    List<Address> selectAddressesByOpenId(@Param("openId")String openId);

    int updateDefaultAddressById(@Param("id")String id);

    int updateNoDefaultAddressByIds(@Param("ids")List<String> ids);
}