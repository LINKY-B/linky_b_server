package com.linkyB.backend.user.exception;

import com.linkyB.backend.common.exception.LInkyBussinessException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends LInkyBussinessException {
    private static final String message = "해당 유저가 이미 존재합니다.";
    private static final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    public UserAlreadyExistException() {
        super(message, httpStatus);
    }
}
