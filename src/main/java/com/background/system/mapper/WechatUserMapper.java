package com.background.system.mapper;

import com.background.system.entity.WechatUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WechatUserMapper extends BaseMapper<WechatUser> {
    int deleteByPrimaryKey(Long id);

    int insert(WechatUser record);

    int insertSelective(@Param("record") WechatUser record);

    WechatUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WechatUser record);

    int updateByPrimaryKey(WechatUser record);

    Boolean selectByOpenId(@Param("openId") String openId);

    int updateUserInfoByOpenId(@Param("openId")String openId,
                               @Param("userInfo")String userInfo);
}