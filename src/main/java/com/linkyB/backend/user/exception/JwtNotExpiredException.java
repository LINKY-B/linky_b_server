package com.linkyB.backend.user.exception;

import com.linkyB.backend.common.exception.LinkyBusinessException;

import static com.linkyB.backend.common.exception.ErrorCode.JWT_NOT_EXPIRED;

public class JwtNotExpiredException extends LinkyBusinessException {
        public JwtNotExpiredException(){
            super(JWT_NOT_EXPIRED);
        }

}
