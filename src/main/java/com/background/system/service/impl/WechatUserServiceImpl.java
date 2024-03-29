package com.background.system.service.impl;

import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.background.system.cache.ConfigCache;
import com.background.system.constant.Constant;
import com.background.system.entity.WechatUser;
import com.background.system.entity.token.Token;
import com.background.system.mapper.WechatUserMapper;
import com.background.system.request.WechatUserInfo;
import com.background.system.service.WechatUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class WechatUserServiceImpl extends BaseService implements WechatUserService {

    @Resource
    private WechatUserMapper wechatUserMapper;

    @Override
    @Cacheable(value = "wechatUser", key = "#id")
    public WechatUser selectByPrimaryKey(Long id) {
        return wechatUserMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public Map<String, Object> wechatLogin(String code) {
        return getOpenIdByCode(code);
    }

    @Override
    @Cacheable(value = "wechatUser", key = "#openId")
    public Boolean selectByOpenId(String openId) {
        return wechatUserMapper.selectByOpenId(openId);
    }

    @Override
    @Transactional
    @Cacheable(value = "wechatUser", key = "#userInfo.openId")
    public Boolean updateUserInfo(WechatUserInfo userInfo) {
        Token currentUser = getWeChatCurrentUser();
        userInfo.setOpenId(currentUser.getUsername());
        if (selectByOpenId(userInfo.getOpenId())) {
            return this.wechatUserMapper.updateUserInfoByOpenId(userInfo.getOpenId(), userInfo.getUserInfo())>0;
        }
        return false;
    }

    public Map<String, Object> getOpenIdByCode(String code) {
        ConcurrentMap<String, String> configMap = ConfigCache.configMap;

//        log.info("configMap[{}]", map);
        String url = String.format(
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                configMap.get("wechat_appid").trim(), configMap.get("wechat_secret").trim(), URLEncoder.encode(code));
//        log.info("code url [{}]", url);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            String r = EntityUtils.toString(entity);
            log.info("小程序获取openId结果:{}", r);
            JSONObject json = JSON.parseObject(r);
            String openId = json.getString("openid");
            if (StringUtils.isEmpty(openId)) {
                log.error("小程序获取openId出错");
                HashMap<String, Object> result = new HashMap<>(1);
                result.put("errcode", json.get("errcode"));
                result.put("errmsg", json.get("errmsg"));
                return result;
            }
            HashMap<String, Object> result = new HashMap<>(1);
            Map<String, Object> params = new HashMap<String, Object>(2) {
                private static final long serialVersionUID = 1L;

                {
                    put("open_id", openId);
                    put("expire_time", Constant.EXPIRE_TIME);
                }
            };
            result.put("token", JWTUtil.createToken(params, openId.getBytes()));

            // select
            if (!wechatUserMapper.selectByOpenId(openId)) {
                WechatUser wechatUser = new WechatUser();
                wechatUser.setOpenId(openId);
                wechatUserMapper.insertSelective(wechatUser);
            }

            return result;
        } catch (Exception e) {
            log.error("小程序获取openId出错", e);
        }
        return null;
    }
}
