package com.linkyB.backend.common.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkyB.backend.common.exception.ErrorResponse;
import com.linkyB.backend.common.exception.LinkyBusinessException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class CustomExceptionHandleFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (LinkyBusinessException e) {
            final ErrorResponse errorCode = ErrorResponse.of(e.getErrorCode());
            response.setStatus(errorCode.getStatus());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try (OutputStream os = response.getOutputStream()) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(os, errorCode);
                os.flush();
            }
        }
    }

}
