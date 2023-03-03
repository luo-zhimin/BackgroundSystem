package com.background.system;

import com.background.system.controller.TimeTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/4 02:24
 */
@SpringBootTest
public class OrderTest {

    @Autowired
    private TimeTask timeTask;

    @Test
    void testSourceZip() {
        timeTask.sourceTask("4720");
    }
}
