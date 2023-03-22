package com.linkyB.backend.user.exception;

import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;

public class JwtExpiredException extends LinkyBusinessException {
    public JwtExpiredException(){
        super(ErrorCode.JWT_EXPIRED);
    }
}
