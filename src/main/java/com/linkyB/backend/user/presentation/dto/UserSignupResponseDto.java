package com.linkyB.backend.user.presentation.dto;

import com.linkyB.backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupResponseDto {
    private String phone;

    public static UserSignupResponseDto of(User user) {
        return new UserSignupResponseDto(user.getUserPhone());
    }
}
