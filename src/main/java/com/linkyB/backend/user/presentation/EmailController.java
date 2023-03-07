package com.linkyB.backend.user.presentation;

import com.linkyB.backend.user.application.EmailService;
import com.linkyB.backend.user.presentation.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/mail")
    public String mailConfirm(@RequestBody EmailDto emailDto) throws MessagingException, UnsupportedEncodingException {

        String authCode = emailService.sendEmail(emailDto.getEmail());
        return authCode;
    }
}