package com.background.system;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2022/9/2 21:22
 */
@SpringBootTest
public class JasyptTest {

    private static final String key = " ";

    @Autowired
    @Qualifier(value = "jasyptStringEncryptor")
    private StringEncryptor stringEncryptor;

    @Test
    void encrypt(){
        List<String> values = Lists.newArrayList("root","XNXxnx520",
                "jdbc:mysql://rm-7xvkbcfxrhi0ht10cto.mysql.rds.aliyuncs.com/backgroundSystem?rewriteBatchedStatements=true&useSSL=false&zeroDateTimeBehavior=convertToNull&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai");
        StringBuilder builder = new StringBuilder();
        PooledPBEStringEncryptor pooledPBEStringEncryptor = new PooledPBEStringEncryptor();
        pooledPBEStringEncryptor.setConfig(cryptOr());
        values.forEach(v->{
            String encrypt = pooledPBEStringEncryptor.encrypt(v);
            builder.append("ENC(").append(encrypt).append(")").append(",");
        });
        //去除最后的,
        String result = builder.substring(0,builder.length()-1);
        System.out.println(result);
    }

//    @Test
     void decry(){
        List<String> values = Lists.newArrayList("ENC(E3yurGED2kuLaz98OObtew==)","ENC(tSN8uRTBnSD/8kjHx0ONd0qAfcReh444)",
                "ENC(iqf1X4Sgdfh25Cbi1HnhNtSuw8tH17HWMb7PRh7BivimhEiCmP0Au169Kp8MtLEXVZGBHfknH/Wv9il0wVlINKME3I/FjFrIh2XJtWFzTJo62CEfS6Z4KmVkclanMKUFlAm1Dx9FuBl2Tnt51bHKVj4KUc+/NGJqus5Kso2LXGLmpDdOwi0K3HgkyyvTASp9rmTzAGGm8OzBT2JlRCCIHf3Xb9EK2fY3NFbBwfRLyJl9nNPYxnCBoQ==)");
        PooledPBEStringEncryptor pooledPBEStringEncryptor = new PooledPBEStringEncryptor();
        pooledPBEStringEncryptor.setConfig(cryptOr());
        values.forEach(v->{
            //replace
            v = v.replaceAll("ENC\\(","");
            v = v.replaceAll("\\)","");
//            System.out.println(pooledPBEStringEncryptor.decrypt(v));
            System.out.println(stringEncryptor.decrypt(v));

//            System.out.println(JasyptUtils.stringEncryptor(key, v, algorithm, type, false));
        });

    }

    @Test
    void showRandomString(){
        //쟕雼췝盪蹙䬱ປ彾 --> 5de88f71a509010ba5a0491c751b2d77
        String random = RandomStringUtils.random(10);
        System.out.println(random);
    }

    public static SimpleStringPBEConfig cryptOr() {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(key);
        config.setAlgorithm(StandardPBEByteEncryptor.DEFAULT_ALGORITHM);
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName(null);
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        return config;
    }

    public static void main(String[] args) {
        new JasyptTest().decry();
    }
}
