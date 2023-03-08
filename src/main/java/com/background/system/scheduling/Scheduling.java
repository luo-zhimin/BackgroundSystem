package com.background.system.scheduling;

import com.background.system.controller.TimeTask;
import com.background.system.service.impl.CouponServiceImpl;
import com.background.system.service.impl.OrderServiceImpl;
import com.background.system.service.impl.SendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/9/22 11:37
 */
@Component
public class Scheduling {

    private static Logger logger = LoggerFactory.getLogger(Scheduling.class);

    @Autowired
    TimeTask timeTask;

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    private CouponServiceImpl couponService;

    @Autowired
    private SendService sendService;

    @Scheduled(fixedDelayString="1800000")
    public synchronized void executeCreateZip(){
        //todo 生成zip文件(若前端进行裁剪，则可以直接用sourceZip,若后端进行裁剪(py),则需要用到第一个)
        logger.info("scheduling execute time[{}]", LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute());
        timeTask.task();
        //若是要使用 则需要将上面的getFile方法取出来，放到这里，进行入参
//        timeTask.sourceTask();
    }

    @Scheduled(cron = "0 58 23 * * ? ")
    public synchronized void closeOrder(){
        logger.info("closeOrder delOrder");
        orderService.closeOrder();
    }

    @Scheduled(cron = "0 0/10 * * * ? ")
    public synchronized void closeCoupon(){
        logger.info("closeCoupon");
        couponService.closeCoupon();
    }

    @Scheduled(cron = "0 58 23 * * ? ")
    public synchronized void sendEmail(){
        logger.info("sendEmail");
        sendService.sendEmail();
    }
}
