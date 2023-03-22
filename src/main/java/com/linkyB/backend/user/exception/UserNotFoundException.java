package com.linkyB.backend.user.exception;

import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;

public class UserNotFoundException extends LinkyBusinessException {


    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

}
