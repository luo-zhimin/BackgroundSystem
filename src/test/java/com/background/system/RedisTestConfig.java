package com.background.system;

import com.background.system.service.impl.PictureServiceImpl;
import com.background.system.service.impl.SizeServiceImpl;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/2 14:56
 */
@SpringBootTest
public class RedisTestConfig {

    @Resource
    private SizeServiceImpl sizeService;

    @Resource
    private PictureServiceImpl pictureService;

    @Test
    public void testSizeDetail(){
        System.out.println("第一次查询");
        System.out.println(sizeService.getSizeDetail("34"));
        System.out.println("第二次查询");
        System.out.println(sizeService.getSizeDetail("34"));
    }

    @Test
    public void testSizeList(){
        System.out.println("第一次查询");
        System.out.println(sizeService.getSizeList(1, 10));
        System.out.println("第二次查询");
        System.out.println(sizeService.getSizeList(1, 10));
    }

    @Test
    public void testPicture(){
        List<String> ids = Lists.newArrayList("189332", "189330");
        System.out.println("第一次查询");
        System.out.println(pictureService.getPicturesByIds(ids));
        System.out.println("第二次查询");
        System.out.println(pictureService.getPicturesByIds(ids));
    }

}
