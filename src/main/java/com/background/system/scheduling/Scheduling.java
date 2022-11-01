package com.background.system.scheduling;

import com.background.system.controller.TimeTask;
import com.background.system.service.impl.OrderServiceImpl;
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

    @Scheduled(cron = "0 0/5 * * * ? ")
    public synchronized void executeCreateZip(){
        logger.info("scheduling execute time[{}]", LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute());
        timeTask.task();
    }

    @Scheduled(cron = "0 0 12 * * ? ")
    public synchronized void closeOrder(){
        orderService.closeOrder();
    }
}
