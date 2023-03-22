package com.linkyB.backend.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtDto {
    private String type;
    private String accessToken;
    private String refreshToken;
}
