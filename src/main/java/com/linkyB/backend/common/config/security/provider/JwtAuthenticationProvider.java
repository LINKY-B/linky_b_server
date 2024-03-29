package com.linkyB.backend.common.config.security.provider;

import com.linkyB.backend.common.config.security.token.JwtAuthenticationToken;
import com.linkyB.backend.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtUtil jwtUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String jwt = (String) authentication.getPrincipal();
        return jwtUtil.getAuthentication(jwt);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
