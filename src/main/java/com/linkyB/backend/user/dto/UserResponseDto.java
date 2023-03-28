package com.linkyB.backend.user.dto;

import com.linkyB.backend.user.domain.User;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long userId;

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .build();
    }
}
