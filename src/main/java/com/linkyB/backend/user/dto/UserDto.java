package com.linkyB.backend.user.dto;

import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class UserDto {
    private Long userId;
}
