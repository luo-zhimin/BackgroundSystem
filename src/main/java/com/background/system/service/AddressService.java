package com.background.system.service;

import com.background.system.entity.Address;
import com.background.system.request.AddressRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/20 11:54
 */
public interface AddressService {

    Boolean insertAddress(AddressRequest request);

    Boolean updateAddress(AddressRequest request);

    Boolean deleteAddress(String id);

    Page<Address> getAddressList(Integer page, Integer size);

    Address getAddressDetail(String id);

    Boolean updateDefaultAddress(String id);
}
