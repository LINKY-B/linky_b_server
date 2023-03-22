package com.linkyB.backend.user.exception;

import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;

public class LogoutByAnotherException extends LinkyBusinessException {
    public LogoutByAnotherException() {
        super(ErrorCode.LOGOUT_BY_ANOTHER);
    }
}
