package org.powernode.springboot.service.mail.impl;

import org.powernode.springboot.service.database.service.redis.RegisterService;
import org.powernode.springboot.service.mail.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private JavaMailSender mailSender;
    Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Autowired
    private RegisterService registerService;

    @Override
    public boolean sendVerification( String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        if(registerService.hasVerifyCode(email))
            return false;
        message.setSubject("图书网站张开开通");
        String token = UUID.randomUUID().toString();
        logger.info("邮箱{}请求创建账号,正在生成验证码",email);
        registerService.setVerifyCode(email, token);
        message.setText("您好，您当前正在注册的是图书网站的账号，您的验证码是:"+token);
        mailSender.send(message);
        return true;
    }
}
