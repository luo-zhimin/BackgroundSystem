package com.background.system;

import com.background.system.entity.Email;
import com.background.system.service.impl.ConfigServiceImpl;
import com.background.system.service.impl.SendService;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/3 11:06
 */
@SpringBootTest
@EnableEncryptableProperties
public class EmailTest {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private ConfigServiceImpl configService;

    @Autowired
    private SendService sendService;

    @Test
    void sendSimpleMail() {
        //goodtan231@outlook.com  admin@asugar.cn
        //读取缓存
        String to = configService.getConfig("send-email");

        System.out.println("to-->"+to);
        String[] addressees = to.split(",");
        Email email = Email.builder()
                .addressees(addressees)
                .title("小程序测试")
                .content("铃星小程序每日一句测试")
                .build();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(email.getAddressees());
        mailMessage.setSubject(email.getTitle());
        mailMessage.setText(email.getContent());

        try {
            mailSender.send(mailMessage);
            System.out.println("发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发送失败");
        }
    }

    @Test
    void sendEmail(){
        System.out.println(sendService.sendEmail());
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

}
