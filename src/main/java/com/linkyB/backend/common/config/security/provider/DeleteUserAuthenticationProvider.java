package com.linkyB.backend.common.config.security.provider;

import com.linkyB.backend.common.config.security.token.DeleteAuthenticationToken;
import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;
import com.linkyB.backend.common.util.JwtUtil;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.exception.UserNotFoundException;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserAuthenticationProvider implements AuthenticationProvider {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DeleteUserAuthenticationProvider(JwtUtil jwtUtil, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String jwt = (String) authentication.getPrincipal();
        final String password = (String) authentication.getCredentials();
        Authentication authUser = jwtUtil.getAuthentication(jwt);
        Long userId = Long.valueOf(authUser.getName());

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!bCryptPasswordEncoder.matches(password, user.getUserPassword())) {
            throw new LinkyBusinessException(ErrorCode.AUTHORITY_INVALID);
        }
        return jwtUtil.getAuthentication(jwt);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return DeleteAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
