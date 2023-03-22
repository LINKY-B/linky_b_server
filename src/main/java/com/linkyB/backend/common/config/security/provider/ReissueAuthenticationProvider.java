package com.linkyB.backend.common.config.security.provider;

import com.linkyB.backend.common.config.security.token.ReissueAuthenticationToken;
import com.linkyB.backend.common.util.JwtUtil;
import com.linkyB.backend.user.service.redis.RefreshTokenService;
import com.linkyB.backend.user.domain.redis.RefreshToken;
import com.linkyB.backend.user.exception.JwtExpiredException;
import com.linkyB.backend.user.exception.JwtInvalidException;
import com.linkyB.backend.user.exception.JwtNotExpiredException;
import com.linkyB.backend.user.exception.LogoutByAnotherException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ReissueAuthenticationProvider implements AuthenticationProvider {
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String refreshTokenString = (String) authentication.getPrincipal();
        final String accessTokenString = (String) authentication.getCredentials();

        log.info("[Reissue Authentication Provider accessToken : {}", accessTokenString);

        // reissue accessToken 검증. 만료된 경우에만 발급받을 수 있도록 함.
        try {
            final Authentication accessTokenAuthentication = jwtUtil.getAuthentication(accessTokenString);
            throw new JwtNotExpiredException();
        } catch (JwtExpiredException jee) {
            log.info("만료된 인증토큰! reissue 에 알맞음.");
        } catch (JwtNotExpiredException e) {
            log.info("만료되지 않은 인증토큰!");
            throw new JwtNotExpiredException();
        } catch (Exception e) {
            log.info("잘못된 인증토큰! {}", e);
            throw new JwtInvalidException();
        }


        final Authentication authenticated = jwtUtil.getAuthentication(refreshTokenString);

        final String memberId = (String) authenticated.getName();

        // 사용할 수 있는(저장된) 토큰인지 확인
        final RefreshToken refreshToken = refreshTokenService.findRefreshToken(Long.valueOf(memberId),
                refreshTokenString).orElseThrow(LogoutByAnotherException::new);

        this.deleteRefreshToken(refreshToken);

        return authenticated;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ReissueAuthenticationToken.class.isAssignableFrom(aClass);
    }

    private void deleteRefreshToken(RefreshToken refreshToken) {
        refreshTokenService.deleteRefreshToken(refreshToken);
    }
}
