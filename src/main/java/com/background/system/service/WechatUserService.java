package com.background.system.service;

import com.background.system.entity.WechatUser;
import com.background.system.request.WechatUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface WechatUserService {

    WechatUser selectByPrimaryKey(Long id);

    Map<String, Object> wechatLogin(String code);

    Boolean selectByOpenId(@Param("openId") String openId);

    Boolean updateUserInfo(WechatUserInfo userInfo);
}
