package com.linkyB.backend.common.config.security.handler;

import com.linkyB.backend.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.linkyB.backend.common.exception.ErrorCode.ACCOUNT_MISMATCH;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        List<ErrorResponse.FieldError> errorList = new ArrayList<>();

        // 이메일 오류로 인한 실패
        if(exception instanceof InternalAuthenticationServiceException) {
            errorList.add(new ErrorResponse.FieldError("email", "", "존재하지 않는 이메일입니다."));
        }

        // 비밀번호 오류로 인한 실패
        if(exception instanceof BadCredentialsException) {
            errorList.add(new ErrorResponse.FieldError("password", "", "잘못된 비밀번호입니다."));
        }

        HandlerUtility.writeResponse(request, response, ACCOUNT_MISMATCH, errorList);
    }
}
