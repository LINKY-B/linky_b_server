package com.linkyB.backend.common.config.security.filter;

import com.linkyB.backend.user.dto.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;


public class CustomUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/auth/login", "POST");
    private final Validator validator;
    final ObjectMapper objectMapper = new ObjectMapper();

    public CustomUsernamePasswordAuthenticationFilter(Validator validator) {
        super(ANT_PATH_REQUEST_MATCHER);
        this.validator = validator;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
            AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            try {
                final String requestBody = IOUtils.toString(request.getReader());
                final LoginRequestDto loginRequest = objectMapper.readValue(requestBody, LoginRequestDto.class);
                Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(loginRequest);

                final UsernamePasswordAuthenticationToken authRequest
                        = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
                return super.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                throw new AuthenticationServiceException("Authentication failed while converting request body.");
            }
        }
    }

}

