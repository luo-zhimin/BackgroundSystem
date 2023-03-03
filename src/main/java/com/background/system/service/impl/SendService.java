package com.background.system.service.impl;

import com.background.system.entity.Email;
import com.background.system.entity.Orderd;
import com.background.system.mapper.OrderMapper;
import com.background.system.util.SendEmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2023/3/3 15:15
 */
@Service
public class SendService {

    private static final Logger logger = LoggerFactory.getLogger(SendService.class);

    @Autowired
    private SendEmailUtil sendEmailUtil;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ConfigServiceImpl configService;

    public String sendEmail() {
        String value = configService.getConfig("send-email");
        Orderd order = orderMapper.getCurrentDay();
        return sendEmailUtil.sendEmail(Email.builder()
                .addressees(value.split(","))
                .title("铃星小程序当日营收")
                .content("铃星小程序"+order.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"营收：" + order.getTotal() + "元，订单数：" + order.getNumber() + "笔")
                .build());
    }
}
