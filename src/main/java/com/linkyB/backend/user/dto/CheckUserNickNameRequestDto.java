package com.linkyB.backend.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CheckUserNickNameRequestDto {
    @NotBlank(message = "닉네임은 필수 입력입니다.")
    String nickName;

}
