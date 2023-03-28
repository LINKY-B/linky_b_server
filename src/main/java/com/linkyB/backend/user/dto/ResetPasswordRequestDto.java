package com.linkyB.backend.user.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Schema(description = "비밀번호 변경 요청 DTO")
public class ResetPasswordRequestDto {

    @ApiModelProperty(value = "이메일", example = "real@naver.com", required = true)
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @ApiModelProperty(value = "인증번호", example = "3A5FD", required = true)
    @NotBlank
    private String authCode;

    @ApiModelProperty(value = "새로운 비밀번호", example = "test-password1!", required = true)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$", message = "비밀번호는 영문, 숫자, 특수문자를 하나씩 포함해야합니다.")
    @Size(min = 7)
    private String password;
}
