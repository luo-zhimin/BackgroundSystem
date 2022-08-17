package com.background.system.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.background.system.mapper.WechatUserMapper;
import com.background.system.entity.WechatUser;
import com.background.system.service.WechatUserService;
@Service
public class WechatUserServiceImpl implements WechatUserService{

    @Resource
    private WechatUserMapper wechatUserMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return wechatUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(WechatUser record) {
        return wechatUserMapper.insert(record);
    }

    @Override
    public int insertSelective(WechatUser record) {
        return wechatUserMapper.insertSelective(record);
    }

    @Override
    public WechatUser selectByPrimaryKey(Long id) {
        return wechatUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(WechatUser record) {
        return wechatUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(WechatUser record) {
        return wechatUserMapper.updateByPrimaryKey(record);
    }

}
