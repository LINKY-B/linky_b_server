package com.linkyB.backend.user.presentation;

import com.linkyB.backend.config.BaseResponse;
import com.linkyB.backend.user.application.EmailService;
import com.linkyB.backend.user.presentation.dto.AuthCodeDto;
import com.linkyB.backend.user.presentation.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RequestMapping("/mail")
@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private String authCode;

    @PostMapping("")
    public BaseResponse<AuthCodeDto> mailConfirm(@RequestBody EmailDto emailDto) throws MessagingException, UnsupportedEncodingException {

        authCode = emailService.sendEmail(emailDto.getEmail());
        AuthCodeDto response = new AuthCodeDto(authCode);
        return new BaseResponse<>(response);
    }

    @GetMapping("")
    public ResponseEntity<?> mailValidation(@RequestBody AuthCodeDto authCodeDto){

        try {
            if(authCode.equals(authCodeDto.getAuthCode())){
                return new ResponseEntity<>("인증번호가 확인되었습니다.", HttpStatus.OK);
            }

            return new ResponseEntity<>("인증번호가 일치하지 않습니다.",HttpStatus.BAD_REQUEST);
        }
        catch (NullPointerException e){
            ;
            return new ResponseEntity<>("인증번호가 전송되지 않았습니다.", HttpStatus.BAD_REQUEST);
        }

    }

}