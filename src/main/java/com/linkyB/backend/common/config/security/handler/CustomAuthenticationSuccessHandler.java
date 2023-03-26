package com.linkyB.backend.common.config.security.handler;

import com.linkyB.backend.user.dto.JwtDto;
import com.linkyB.backend.user.dto.JwtResponseDto;
import com.linkyB.backend.user.service.redis.RefreshTokenService;
import com.linkyB.backend.common.result.ResultCode;
import com.linkyB.backend.common.result.ResultResponse;
import com.linkyB.backend.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    private final ResultCode DEFAULT_RESULT_CODE = ResultCode.LOGIN_SUCCESS;

    Map<String, ResultCode> resultCodeMap = new HashMap<>();

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Integer REFRESH_TOKEN_VALIDATION_TIME_IN_SECONDS;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        final JwtDto jwtDto = jwtUtil.createJwt(authentication);
        final JwtResponseDto jwtResponseDto = JwtResponseDto.builder()
                .type(jwtDto.getType())
                .accessToken(jwtDto.getAccessToken())
                .build();

        addRefreshTokenCookie(response, jwtDto.getRefreshToken());
        refreshTokenService.addRefreshToken(Long.valueOf(authentication.getName()), jwtDto.getRefreshToken());

        HandlerUtility.writeResponse(request, response, ResultResponse.of(getResultCode(request), jwtResponseDto));
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken){
        final Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setMaxAge(REFRESH_TOKEN_VALIDATION_TIME_IN_SECONDS);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    public void setResultCodeMap(Map<String, ResultCode> resultCodeMap) {
        this.resultCodeMap = resultCodeMap;
    }

    private ResultCode getResultCode(HttpServletRequest request) {
        if (resultCodeMap != null && resultCodeMap.containsKey(request.getRequestURI())) {
            return resultCodeMap.get(request.getRequestURI());
        } else {
            return DEFAULT_RESULT_CODE;
        }
    }
}
