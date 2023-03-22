package com.linkyB.backend.user.service;

import com.linkyB.backend.user.domain.redis.EmailCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    private static final String fromEmail = "ingistak@gmail.com";
    private static final String title = "Linky-B 회원가입 인증 번호"; //제목

    //메일 양식 작성
    public MimeMessage createEmailForm(EmailCode emailCode) throws MessagingException, UnsupportedEncodingException {
        final String toEmail = emailCode.getEmail();
        final String authCode = emailCode.getCode();

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setFrom(fromEmail); //보내는 이메일
        message.setText(setContext(authCode), "utf-8", "html");

        return message;
    }

    public void sendEmail(EmailCode emailCode) throws MessagingException, UnsupportedEncodingException {
        MimeMessage emailForm = createEmailForm(emailCode);
        emailSender.send(emailForm);
    }

    //실제 메일 전송
    public void sendEmail(MimeMessage emailForm) throws MessagingException, UnsupportedEncodingException {
        emailSender.send(emailForm);
    }

    //타임리프를 이용한 context 설정
    public String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("mail", context); //mail.html
    }

}
