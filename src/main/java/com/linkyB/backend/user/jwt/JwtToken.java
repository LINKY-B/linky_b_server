package com.linkyB.backend.user.jwt;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {

    private String grantType; //jwt에 대한 인증 타입
    private String accessToken;
    private String refreshToken;
}
