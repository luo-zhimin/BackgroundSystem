package com.background.system.service.impl;

import com.background.system.entity.Address;
import com.background.system.entity.token.Token;
import com.background.system.exception.ServiceException;
import com.background.system.mapper.AddressMapper;
import com.background.system.request.AddressRequest;
import com.background.system.service.AddressService;
import com.background.system.service.BaseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
@Service
@Slf4j
public class AddressServiceImpl extends BaseService implements AddressService {

    @Resource
    private AddressMapper addressMapper;

    @Override
    @Transactional
    public Boolean insertAddress(AddressRequest request) {
        log.info("insertAddress request[{}]",request);
        Token weChatCurrentUser = getWeChatCurrentUser();
        request.setOpenid(weChatCurrentUser.getUsername());
        request.setIsDel(false);
        request.setCreateTime(LocalDateTime.now());
        return insert(request)>0;
    }

    @Override
    @Transactional
    public Boolean updateAddress(AddressRequest request) {
        log.info("updateAddress request[{}]",request);
        //校验
        if (checkUser(Long.parseLong(request.getId()))) {
            return updateByPrimaryKeySelective(request)>0;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean deleteAddress(Long id) {
        log.info("deleteAddress id[{}]",id);
        return addressMapper.deleteAddressById(id)>0;
    }

    @Override
    public Page<Address> getAddressList(Integer page, Integer size) {
        Token currentUser = getWeChatCurrentUser();
        Long count = addressMapper.selectCount(
                new QueryWrapper<Address>()
                        .eq("is_del", Boolean.FALSE)
                        .eq("openId", currentUser.getUsername())
        );
        List<Address> addresses = addressMapper.getAddressList((page - 1) * size,
                size, currentUser.getUsername());
        Page<Address> addressPage = new Page<>();
        addressPage.setTotal(count);
        addressPage.setRecords(addresses);
        addressPage.setPages(page);
        addressPage.setSize(size);
        return addressPage;
    }

    @Override
    public Address getAddressDetail(Long id) {
        return selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public Boolean updateDefaultAddress(Long id) {
        Token weChatCurrentUser = getWeChatCurrentUser();
        if (checkUser(id)) {
            //所有的address 该用户的
            List<Address> addresses = addressMapper.selectAddressesByOpenId(weChatCurrentUser.getUsername());
            //修改其他为非默认地址
            addresses = addresses.stream().filter(address -> !address.getId().equals(id+"")).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(addresses)){
                addressMapper.updateNoDefaultAddressByIds(addresses.stream().map(Address::getId).collect(Collectors.toList()));
            }
            return addressMapper.updateDefaultAddressById(id)>0;
        }
        return false;
    }

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

    /**
     * 检查是否是自己的地址
     * @return 是否
     */
    private Boolean checkUser(Long id){
        if (id == null) {
            throw new ServiceException(1000,"id 不可以为空");
        }
        Token weChatCurrentUser = getWeChatCurrentUser();
        Address address = selectByPrimaryKey(id);
        if (address == null){
            throw new ServiceException(1101,"请选择确认您选择的当前地址是否正确");
        }
        //暂时只校验微信小程序
        return weChatCurrentUser.getUsername().equals(address.getOpenid());
    }
}
