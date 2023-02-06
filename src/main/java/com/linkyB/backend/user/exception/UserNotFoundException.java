package com.linkyB.backend.user.exception;

import com.linkyB.backend.common.exception.LInkyBussinessException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends LInkyBussinessException {
    private static final String message = "유저가 존재하지 않습니다.";
    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;


    public UserNotFoundException() {
        super(message, httpStatus);
    }

}
