package com.linkyB.backend.user.presentation;


import com.linkyB.backend.user.application.EmailService;
import com.linkyB.backend.user.presentation.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequiredArgsConstructor

public class EmailController {

    private final EmailService emailService;

    @PostMapping("/mail")
    public ResponseEntity<String> mailConfirm(@RequestBody EmailDto emailDto) throws MessagingException, UnsupportedEncodingException {
        String authCode = emailService.sendEmail(emailDto.getEmail());
        return ResponseEntity.ok(authCode);
    }
}