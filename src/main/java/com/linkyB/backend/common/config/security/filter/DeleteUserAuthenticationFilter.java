package com.linkyB.backend.common.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkyB.backend.common.config.security.handler.HandlerUtility;
import com.linkyB.backend.common.config.security.token.DeleteAuthenticationToken;
import com.linkyB.backend.common.result.ResultCode;
import com.linkyB.backend.common.result.ResultResponse;
import com.linkyB.backend.common.util.JwtUtil;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.dto.DeleteUserRequestDto;
import com.linkyB.backend.user.exception.UserNotFoundException;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class DeleteUserAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final AntPathRequestMatcher ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/users", "DELETE");
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    final ObjectMapper objectMapper = new ObjectMapper();

    public DeleteUserAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        super(ANT_PATH_REQUEST_MATCHER);
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("delete user authentication filter!");
        final String accessToken = jwtUtil.extractJwt(request);

        if (!request.getMethod().equals("DELETE")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try {
            final String requestBody = IOUtils.toString(request.getReader());
            final DeleteUserRequestDto deleteRequest = objectMapper.readValue(requestBody, DeleteUserRequestDto.class);
            final DeleteAuthenticationToken authRequest = new DeleteAuthenticationToken(accessToken, deleteRequest.getPassword());

            return super.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Authentication failed while converting request body.");
        }
    }

    @Override
    @Transactional
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        Long userId = Long.valueOf(authentication.getName());
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.deleteUser();
        userRepository.saveAndFlush(user);

        HandlerUtility.writeResponse(request, response, ResultResponse.of(ResultCode.DELETE_USER_SUCCESS));
    }


}
