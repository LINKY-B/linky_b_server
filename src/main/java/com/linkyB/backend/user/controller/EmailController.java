package com.linkyB.backend.user.controller;

import com.linkyB.backend.common.result.ResultResponse;
import com.linkyB.backend.user.service.EmailService;
import com.linkyB.backend.user.dto.AuthCodeDto;
import com.linkyB.backend.user.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

import static com.linkyB.backend.common.result.ResultCode.*;

@RequestMapping("/mail")
@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private String authCode;

    @PostMapping("")
    public ResultResponse<AuthCodeDto> mailConfirm(@RequestBody EmailDto emailDto) throws MessagingException, UnsupportedEncodingException {

        authCode = emailService.sendEmail(emailDto.getEmail());
        AuthCodeDto response = new AuthCodeDto(authCode);
        return new ResultResponse<>(SEND_CONFIRM_EMAIL_SUCCESS, response);
    }

    @GetMapping("")
    public ResultResponse mailValidation(@RequestBody AuthCodeDto authCodeDto) {

        try {
            if (authCode.equals(authCodeDto.getAuthCode())) {
                return ResultResponse.of(CONFIRM_EMAIL_SUCCESS);
            }

            return ResultResponse.of(CONFIRM_EMAIL_FAIL);
        } catch (NullPointerException e) {
            ;
            return ResultResponse.of(CONFIRM_EMAIL_FAIL);
        }

    }

}