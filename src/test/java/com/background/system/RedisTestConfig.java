package com.background.system;

import com.background.system.service.impl.MaterialQualityServiceImpl;
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

    @Resource
    private MaterialQualityServiceImpl materialQualityService;

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


    //redis 多个key组合查询 key为数组 如何缓存
    @Test
    public void testPicture(){
        List<String> ids = Lists.newArrayList("189332", "189330");
        System.out.println("第一次查询");
        System.out.println(pictureService.getPicturesByIds(ids));
        System.out.println("第二次查询");
        System.out.println(pictureService.getPicturesByIds(ids));
    }


    @Test
    public void testMaterialQuality(){
        List<String> ids = Lists.newArrayList("25", "28");
        System.out.println("第一次查询");
        System.out.println(materialQualityService.getMaterialListByIds(ids));
        System.out.println("第二次查询");
        System.out.println(materialQualityService.getMaterialListByIds(ids));
    }
}
