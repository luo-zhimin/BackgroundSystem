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

    private static final String key = "5de88f71a509010ba5a0491c751b2d77";

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

    @Test
     void decry(){
        //QTiY5V4X87ARB6MFvaRGKbnXrQ8KZiYg
        List<String> values = Lists.newArrayList("ENC(yYyhCiJrQK/rhL69R4y1vqJp2Y7JlTkG)","ENC(p7tzRJgmD2r+2cfBc55DfEY2i8VHtpnTMreGrTEQHGM=)",
                "ENC(ZSYUag+xsz7GgoYU08vMUH+tHqiYW+H1stPbtVWpyzH+kI4st9pc20BTPtN80W0WMMHe0ODi2yajsf9MNNSwpHipNt7h2VKJ+LpDtX3a2OnQA4bSlT1oHc82jxSuRMSOxtY2ounon3pO9z5kyelAodlqzlAoBONjn4W+z75Basb7l+tJPEQYYC2eoKfb/+iOM2jhN0UV6V0MQAxn69diDGLWK7YLmnP1i+Ab9drzH/VLBLAf7OSl0IWnniKcrAAdhtKJzW1x9bYGOXjrwrAp36cRG0uTSC5YyqsxAp6ljDg=)");
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
        config.setKeyObtentionIterations(StandardPBEByteEncryptor.DEFAULT_KEY_OBTENTION_ITERATIONS);
        config.setPoolSize("1");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        return config;
    }

    public static void main(String[] args) {
        new JasyptTest().decry();
    }
}
