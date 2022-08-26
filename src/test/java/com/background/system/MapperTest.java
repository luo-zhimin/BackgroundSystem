package com.background.system;

import com.background.system.mapper.WechatUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/8/26 10:49
 */
@SpringBootTest
public class MapperTest {

    @Autowired
    private WechatUserMapper wechatUserMapper;

    @Test
    void testWechat(){
        Boolean aBoolean = wechatUserMapper.selectByOpenId("oZQcV4ypfwUrBm92ZTFgShwweAro");
        System.out.println(aBoolean);
        if (!aBoolean){
            System.out.println("~~~~");
        }
    }
}
