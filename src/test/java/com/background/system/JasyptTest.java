package com.background.system;

import com.google.common.collect.Lists;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
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
@EnableEncryptableProperties
public class JasyptTest {

    private static final String key = "5de88f71a509010ba5a0491c751b2d77";

    @Autowired
    @Qualifier(value = "jasyptStringEncryptor")
    private StringEncryptor stringEncryptor;

    @Test
    void encrypt(){
        List<String> values = Lists.newArrayList("1798677862@qq.com");
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
        //ENC(yOcA+pTrPhZyuJKlBQ52O5MedEIQe7Dl1SFzBTu0R1nqZdiwjz3gMg==),ENC(nNjoKm63Ltqm2/LnKb3p9eitKY0lj8xaxlKalCfNfExtMN9Drz5I9Q==)
        List<String> values = Lists.newArrayList("ENC(yOcA+pTrPhZyuJKlBQ52O5MedEIQe7Dl1SFzBTu0R1nqZdiwjz3gMg==)","ENC(nNjoKm63Ltqm2/LnKb3p9eitKY0lj8xaxlKalCfNfExtMN9Drz5I9Q==)");
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