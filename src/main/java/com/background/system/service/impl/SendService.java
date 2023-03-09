package com.background.system.service.impl;

import com.background.system.entity.Email;
import com.background.system.entity.Orderd;
import com.background.system.mapper.OrderMapper;
import com.background.system.response.CurrentOrderResponse;
import com.background.system.util.CommonUtils;
import com.background.system.util.ExcelUtil;
import com.background.system.util.SendEmailUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
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
        String orderIds = orderMapper.getCurrentDayOrderIds();
        List<String> targets = CommonUtils.getTargets(orderIds);
        String excel = "";
        if (CollectionUtils.isNotEmpty(targets)) {
            List<CurrentOrderResponse> currentOrders = orderMapper.getCurrentOrders(targets);
            ExcelUtil<CurrentOrderResponse> util = new ExcelUtil<>(CurrentOrderResponse.class);
            try {
                excel = util.exportExcel(currentOrders, "当日营收汇总");
                logger.info("send mail excel {}",excel);
            } catch (Exception e) {
                logger.error("导出excel失败", e);
            }
        }

        if (order == null) {
            order = new Orderd();
            order.setTotal(new BigDecimal(0));
            order.setNumber(0);
        }

        return sendEmailUtil.sendEmail(Email.builder()
                .addressees(value.split(","))
                .title("铃星小程序当日营收")
                .attachmentName("当日营收汇总.xlsx")
                .attachmentPath(excel)
                .content("铃星小程序" + order.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "营收：" + order.getTotal() + "元，订单数：" + order.getNumber() + "笔")
                .build());
    }
}
