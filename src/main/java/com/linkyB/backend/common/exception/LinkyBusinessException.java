package com.linkyB.backend.common.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LinkyBusinessException extends RuntimeException{

    private ErrorCode errorCode;
    private List<ErrorResponse.FieldError> errors = new ArrayList<>();

    public LinkyBusinessException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public LinkyBusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
