package com.linkyB.backend.user.exception;

import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;

public class JwtInvalidException extends LinkyBusinessException {
    public JwtInvalidException(){
        super(ErrorCode.JWT_INVALID);
    }
}
