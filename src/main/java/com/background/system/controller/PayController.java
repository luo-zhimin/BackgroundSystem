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
import com.background.system.service.PayService;
import com.background.system.service.impl.BaseService;
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
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.UUID;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/24 6:42 PM
 */
@Api(tags = "支付管理")
@RestController
@RequestMapping("/jsapi/pay")
public class PayController{

    @Autowired
    private PayService payService;

    @GetMapping("/createNo")
    @ApiOperation("JSAPI下单")
    public Result<?> createPay(String orderId){
        return Result.success(payService.createPay(orderId));
    }


    @ApiOperation("唤醒支付需要的paySign")
    @GetMapping("/wakePay")
    public Result<?> wakePay(String prepay_id) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {


        return Result.success(payService.wakePay(prepay_id));
    }

    @ApiOperation("支付成功后调这个")
    @GetMapping("/payOk")
    public Result<?> payOk(String orderId) {

        return Result.success(payService.payOk(orderId));
    }
}
