package com.background.system.util;

import com.background.system.entity.Email;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 *
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

        File file = null;
        if (StringUtils.isNotEmpty(email.getAttachmentName()) && StringUtils.isNotEmpty(email.getAttachmentPath())) {
            file = new File(email.getAttachmentPath());
        }
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(from);
            messageHelper.setTo(email.getAddressees());
            messageHelper.setSubject(email.getTitle());
            messageHelper.setText(email.getContent());
            if (file != null) messageHelper.addAttachment(email.getAttachmentName(), file);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            mailSender.send(message);
            if (file != null) file.delete();
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "发送失败";
        }
    }
}
