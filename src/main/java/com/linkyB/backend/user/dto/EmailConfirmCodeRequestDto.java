package com.linkyB.backend.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class EmailConfirmCodeRequestDto {
    @NotBlank(message = "이메일은 필수 입력입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "사용자 닉네임은 필수 입력입니다.")
    private String userNickName;

    @NotBlank(message = "인증코드는 필수 입력입니다.")
    private String authCode;
}
