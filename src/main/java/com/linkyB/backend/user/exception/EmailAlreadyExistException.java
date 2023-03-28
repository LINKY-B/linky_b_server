package com.linkyB.backend.user.exception;

import com.linkyB.backend.common.exception.LinkyBusinessException;

import static com.linkyB.backend.common.exception.ErrorCode.EMAIL_ALREADY_EXIST;

public class EmailAlreadyExistException extends LinkyBusinessException {

    public EmailAlreadyExistException() {
        super(EMAIL_ALREADY_EXIST);
    }
}
