package com.background.system.util;

import com.background.system.entity.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2023/3/3 15:12
 */
@Component
public class SendEmailUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public String sendEmail(Email email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(email.getAddressees());
        mailMessage.setSubject(email.getTitle());
        mailMessage.setText(email.getContent());

        try {
            mailSender.send(mailMessage);
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "发送失败";
        }
    }
}
