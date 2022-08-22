package com.background.system.service.impl;

import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.background.system.entity.Config;
import com.background.system.entity.WechatUser;
import com.background.system.exception.ServiceException;
import com.background.system.mapper.WechatUserMapper;
import com.background.system.request.WechatUserInfo;
import com.background.system.service.BaseService;
import com.background.system.service.WechatUserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WechatUserServiceImpl extends BaseService implements WechatUserService {

    @Resource
    private WechatUserMapper wechatUserMapper;
    @Resource
    private ConfigServiceImpl configService;

    private Map<String, String> configMap = Maps.newHashMap();

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

    @Override
    @Transactional
    public Map<String, Object> wechatLogin(String code) {
        return getOpenIdByCode(code);
    }

    @Override
    public Boolean selectByOpenId(String openId) {
        return wechatUserMapper.selectByOpenId(openId);
    }

    @Override
    @Transactional
    public Boolean updateUserInfo(WechatUserInfo userInfo) {
        if (StringUtils.isEmpty(userInfo.getOpenId())){
            throw new ServiceException(1000,"openId不可以为空！");
        }
        if (selectByOpenId(userInfo.getOpenId())) {
            return this.wechatUserMapper.updateUserInfoByOpenId(userInfo.getOpenId(), userInfo.getUserInfo())>0;
        }
        return false;
    }

    private Map<String, Object> getOpenIdByCode(String code) {
        Map<String, String> map = getConfigMap();
//        log.info("configMap[{}]", map);
        String url = String.format(
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
            map.get("wechat_appid").trim(), map.get("wechat_secret").trim(), URLEncoder.encode(code));
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
            long expireTime = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15;
            Map<String, Object> params = new HashMap<String, Object>(2) {
                private static final long serialVersionUID = 1L;

                {
                    put("open_id", openId);
                    put("expire_time", expireTime);
                }
            };
            result.put("token", JWTUtil.createToken(params, openId.getBytes()));

            // select
            if (!wechatUserMapper.selectByOpenId(openId)) {
                WechatUser wechatUser = new WechatUser();
                wechatUser.setOpenId(openId);
                wechatUser.setIsDel("0");
                wechatUserMapper.insert(wechatUser);
            }

            return result;
        } catch (Exception e) {
            log.error("小程序获取openId出错", e);
        }
        return null;
    }

    private Map<String, String> getConfigMap() {
        if (configMap != null && configMap.values().size() >= 2) {
            return configMap;
        }
        //init configMap
        List<Config> configs = configService.getConfigsByKeys(Lists.newArrayList("wechat_appid", "wechat_secret"));
        if (CollectionUtils.isNotEmpty(configs)) {
            configMap = configs.stream().collect(Collectors.toMap(Config::getConfigKey, Config::getConfigValue));
        }
        return configMap;
    }
}
