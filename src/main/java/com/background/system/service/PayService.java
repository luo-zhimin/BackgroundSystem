package com.background.system.service;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/8/26 10:31
 */
public interface PayService {

    Map<String,Object> createPay(String orderId);

    Map<String,String> wakePay(String prepay_id);

    Boolean payOk(String orderId);
}
