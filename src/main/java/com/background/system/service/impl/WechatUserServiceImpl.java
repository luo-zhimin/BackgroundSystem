package com.background.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.background.system.entity.Config;
import com.background.system.entity.WechatUser;
import com.background.system.mapper.ConfigMapper;
import com.background.system.mapper.WechatUserMapper;
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

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WechatUserServiceImpl implements WechatUserService {

    @Resource
    private WechatUserMapper wechatUserMapper;
    @Resource
    private ConfigMapper configMapper;

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
    public Map<String, Object> wechatLogin(String code) {
        return getOpenIdByCode(code);
    }

    private Map<String, Object> getOpenIdByCode(String code) {
        Map<String, String> map = getConfigMap();
        log.info("configMap[{}]", map);
        String url = String.format(
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
            map.get("wechat_appid").trim(), map.get("wechat_secret").trim(), URLEncoder.encode(code));
        log.info("code url [{}]", url);
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
            result.put("returnObj", json);
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
        List<Config> configs = configMapper.getConfigsByKeys(Lists.newArrayList("wechat_appid", "wechat_secret"));
        if (CollectionUtils.isNotEmpty(configs)) {
            configMap = configs.stream().collect(Collectors.toMap(Config::getConfigKey, Config::getConfigValue));
        }
        return configMap;
    }
}
