package com.background.system.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.background.system.cache.ConfigCache;
import com.background.system.entity.Coupon;
import com.background.system.entity.Orderd;
import com.background.system.entity.Size;
import com.background.system.entity.token.Token;
import com.background.system.mapper.CouponMapper;
import com.background.system.mapper.OrderMapper;
import com.background.system.mapper.SizeMapper;
import com.background.system.service.CouponService;
import com.background.system.service.PayService;
import com.background.system.util.WxUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/8/26 10:31
 */
@Service
@Slf4j
public class PayServiceImpl extends BaseService implements PayService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private SizeMapper sizeMapper;

    @Resource
    private CouponService couponService;

    @Resource
    private CouponMapper couponMapper;

    @Override
    @Transactional
    @SneakyThrows
    public Map<String, Object> createPay(String orderId) {
        log.info("createPay[{}]",orderId);
        // 获取用户id
        Token user = getWeChatCurrentUser();
        String openId = user.getUsername();

        // 获取订单金额
        Orderd orderd = orderMapper.selectByPrimaryKey(orderId);
        BigDecimal total = orderd.getTotal();

        // 前端判断运费是 0 或者 多少
        total = total.add(orderd.getPortPrice());

        //检查是否有优惠卷
        if (orderd.getCouponId()!=null && orderd.getCouponId()!=0){
            Coupon coupon = couponService.getCouponDetail(orderd.getCouponId());
            total = total.subtract(coupon.getPrice());
        }

        // 获取订单信息
        Size size = sizeMapper.selectById(orderd.getSizeId());

        // 自动获取微信证书, 第一次获取证书绕过鉴权
        CertificatesManager instance = CertificatesManager.getInstance();

        String merchantId = ConfigCache.configMap.get("merchantId");
        String serialNumber = ConfigCache.configMap.get("serialNumber");
        String apiV3Key = ConfigCache.configMap.get("apiV3Key");
        String wechatAppId = ConfigCache.configMap.get("wechat_appid");

        instance.putMerchant(merchantId, new WechatPay2Credentials(merchantId,
                        new PrivateKeySigner(serialNumber,
                                WxUtils.getPrivateKey())),
                apiV3Key.getBytes(StandardCharsets.UTF_8));

        Verifier verifier = instance.getVerifier(merchantId);

        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(merchantId, serialNumber,
                        WxUtils.getPrivateKey()).withValidator(new WechatPay2Validator(verifier));

        CloseableHttpClient httpClient = builder.build();

        // 构建订单数据
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        double total_fee = total.doubleValue() * 100;

        ObjectNode rootNode = objectMapper.createObjectNode();
        String wx_no = UUID.randomUUID().toString().replaceAll("-", "");
        rootNode.put("mchid", merchantId)
                .put("appid", wechatAppId)
                .put("description", size.getTitle())
                .put("notify_url", "http://127.0.0.1:8100/jsapi/pay/notify")
                .put("out_trade_no", wx_no);
        rootNode.putObject("amount")
                .put("total", (int) total_fee);
        rootNode.putObject("payer")
                .put("openid", openId);

        objectMapper.writeValue(bos, rootNode);

        // 回写微信支付订单号
        orderd.setWxNo(wx_no);
        orderd.setIsPay(false);  // 支付状态
        orderd.setStatus("0");     // 发货状态
        orderMapper.updateByPrimaryKeySelective(orderd);

        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String res = EntityUtils.toString(response.getEntity());
        @SuppressWarnings({"all"})
        JSONObject jsonObject = new JSONObject(res);
        return new HashMap<String, Object>() {{
            put("prepay_id", jsonObject.get("prepay_id"));
        }};
    }

    @Override
    @SneakyThrows
    public Map<String, String> wakePay(String prepay_id) {
        log.info("wakePay[{}]",prepay_id);
        // 构建数据结构
        long time = DateUtil.currentSeconds();
        String nonstr = RandomUtil.randomString(20);
        String pack = "prepay_id=" + prepay_id;
        String t = ConfigCache.configMap.get("wechat_appid") + "\n";
        t += +time + "\n";
        t += nonstr + "\n";
        t += pack + "\n";

        // 签名
        PrivateKey privateKey = WxUtils.getPrivateKey();
        String algorithm = ConfigCache.configMap.get("algorithm");
        Signature rsa = Signature.getInstance(algorithm);
        rsa.initSign(privateKey);
        rsa.update(t.getBytes(StandardCharsets.UTF_8));
        byte[] sign = rsa.sign();

        // 唤醒需要的paySign 生成完毕
        String paySign = Base64.getEncoder().encodeToString(sign);

        return new HashMap<String, String>() {{
            put("paySign", paySign);
            put("timeStamp", "" + time);
            put("nonceStr", nonstr);
            put("package", pack);
            put("signType", "RSA");
        }};
    }

    @Override
    @Transactional
    public Boolean payOk(String orderId) {
        log.info("payOk [{}]",orderId);
        Orderd orderd = orderMapper.selectByPrimaryKey(orderId);
        orderd.setIsPay(true);
        //检查是否有优惠卷
        if (orderd.getCouponId()!=null && orderd.getCouponId()!=0){
            Coupon coupon = couponService.getCouponDetail(orderd.getCouponId());
            if (coupon!=null){
                coupon.setIsUsed(true);
                couponMapper.updateByPrimaryKeySelective(coupon);
            }
        }
        return orderMapper.updateByPrimaryKey(orderd)>0;
    }
}
