package com.linkyB.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImgDto {
    @NotBlank(message = "profileImage URL은 필수 필드입니다!")
    private String profileImg;
}
