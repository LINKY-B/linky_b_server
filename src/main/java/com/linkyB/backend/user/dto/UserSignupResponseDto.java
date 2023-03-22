package com.linkyB.backend.user.dto;

import com.linkyB.backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupResponseDto {
    private String email;

    public static UserSignupResponseDto of(User user) {
        return new UserSignupResponseDto(user.getUserEmail());
    }
}
