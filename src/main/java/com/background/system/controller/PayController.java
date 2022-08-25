package com.background.system.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.background.system.cache.ConfigCache;
import com.background.system.entity.Order;
import com.background.system.entity.Size;
import com.background.system.entity.token.Token;
import com.background.system.mapper.OrderMapper;
import com.background.system.mapper.SizeMapper;
import com.background.system.service.BaseService;
import com.background.system.util.Result;
import com.background.system.util.WxUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/24 6:42 PM
 */
@Api(tags = "支付管理")
@RestController
@RequestMapping("/jsapi/pay")
public class PayController extends BaseService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private SizeMapper sizeMapper;

    private ConcurrentMap<String,String> configMap = ConfigCache.configMap;

    @GetMapping("/createNo")
    @ApiOperation("JSAPI下单")
    public Result<?> createPay(String orderId) throws IOException, GeneralSecurityException, HttpCodeException, NotFoundException {
        // 获取用户id
        Token user = getWeChatCurrentUser();
        String openId = user.getUsername();

        // 获取订单金额
        Order order = orderMapper.selectById(orderId);
        BigDecimal total = order.getTotal();

        // 获取订单信息
        Size size = sizeMapper.selectById(order.getSizeId());

        // 自动获取微信证书, 第一次获取证书绕过鉴权
        CertificatesManager instance = CertificatesManager.getInstance();

        String merchantId = configMap.get("merchantId");
        String serialNumber = configMap.get("serialNumber");
        String apiV3Key = configMap.get("apiV3Key");
        String wechatAppId = configMap.get("wechat_appid");

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


        ObjectNode rootNode = objectMapper.createObjectNode();
        String wx_no = UUID.randomUUID().toString().replaceAll("-", "");
        rootNode.put("mchid", merchantId)
                .put("appid", wechatAppId)
                .put("description", size.getTitle())
                .put("notify_url", "http://127.0.0.1:8100/jsapi/pay/notify")
                .put("out_trade_no", wx_no);
        rootNode.putObject("amount")
                .put("total", total);
        rootNode.putObject("payer")
                .put("openid", openId);

        objectMapper.writeValue(bos, rootNode);

        // 回写微信支付订单号
        order.setWxNo(wx_no);
        order.setIsPay(false);  // 支付状态
        order.setStatus("0");     // 发货状态
        orderMapper.updateById(order);

        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String res = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = new JSONObject(res);
        return Result.success(new HashMap<String, Object>() {{
            put("prepay_id", jsonObject.get("prepay_id"));
        }});
    }


    @ApiOperation("唤醒支付需要的paySign")
    @GetMapping("/wakePay")
    public Result<?> wakePay(String prepay_id) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        // 构建数据结构
        long time = DateUtil.currentSeconds();
        String nonstr = RandomUtil.randomString(20);
        String pack = "prepay_id=" + prepay_id;
        String t = configMap.get("wechat_appid")+"\n";
        t += +time + "\n";
        t += nonstr + "\n";
        t += pack + "\n";

        // 签名
        PrivateKey privateKey = WxUtils.getPrivateKey();
        String algorithm = configMap.get("algorithm");
        Signature rsa = Signature.getInstance(algorithm);
        rsa.initSign(privateKey);
        rsa.update(t.getBytes(StandardCharsets.UTF_8));
        byte[] sign = rsa.sign();

        // 唤醒需要的paySign 生成完毕
        String paySign = Base64.getEncoder().encodeToString(sign);
        return Result.success(new HashMap<String, String>() {{
            put("paySign", paySign);
            put("timeStamp", "" + time);
            put("nonceStr", nonstr);
            put("package", pack);
            put("signType", "RSA");
        }});
    }

    @ApiOperation("支付成功后调这个")
    @GetMapping("/payOk")
    public Result<?> payOk(String orderId) {
        Order order = orderMapper.selectById(orderId);
        order.setIsPay(true);
        return Result.success(orderMapper.updateById(order));
    }
}
