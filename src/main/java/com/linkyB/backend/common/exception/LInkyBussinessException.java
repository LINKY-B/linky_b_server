package com.linkyB.backend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LInkyBussinessException extends RuntimeException{
    private final String clientMessage;
    private final HttpStatus httpStatus;


    public LInkyBussinessException(String clientMessage, HttpStatus httpStatus) {
        super(clientMessage); //RuntimeException의 생성자 호출
        this.clientMessage = clientMessage;
        this.httpStatus = httpStatus;
    }
}
