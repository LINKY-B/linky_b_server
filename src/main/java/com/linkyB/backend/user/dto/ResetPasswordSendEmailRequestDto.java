package com.linkyB.backend.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class ResetPasswordSendEmailRequestDto {

    @Email
    String email;
}
