package com.background.system.service.admin.impl;

import com.background.system.entity.vo.AdminUserVO;
import com.background.system.mapper.admin.AdminUserMapper;
import com.background.system.service.admin.IAdminUseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminUseServiceImpl implements IAdminUseService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public Boolean queryUser(String userName, String password) {
        return adminUserMapper.queryUser(userName, password);
    }

    @Override
    public AdminUserVO getAdminInfo() {
        return adminUserMapper.getAdminInfo();
    }
}
