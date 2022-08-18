package com.background.system.service;

import com.background.system.entity.WechatUser;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface WechatUserService {

    int deleteByPrimaryKey(Long id);

    int insert(WechatUser record);

    int insertSelective(WechatUser record);

    WechatUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WechatUser record);

    int updateByPrimaryKey(WechatUser record);

    Map<String, Object> wechatLogin(String code);

    Boolean selectByOpenId(@Param("openId") String openId);
}
