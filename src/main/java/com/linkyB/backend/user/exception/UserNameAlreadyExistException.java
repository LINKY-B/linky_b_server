package com.linkyB.backend.user.exception;

import com.linkyB.backend.common.exception.LinkyBusinessException;

import static com.linkyB.backend.common.exception.ErrorCode.USERNAME_ALREADY_EXIST;

public class UserNameAlreadyExistException extends LinkyBusinessException {

    public UserNameAlreadyExistException() {
        super(USERNAME_ALREADY_EXIST);
    }
}
