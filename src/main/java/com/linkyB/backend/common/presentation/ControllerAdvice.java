package com.linkyB.backend.common.presentation;

import com.linkyB.backend.common.dto.ExceptionResponse;
import com.linkyB.backend.common.exception.LInkyBussinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(LInkyBussinessException.class)
    public ResponseEntity<ExceptionResponse> businessException(final LInkyBussinessException e) {
        log.info("{} : {}", ((Exception)e).getClass().getSimpleName(), e.getMessage());
        return toResponseEntity(e.getClientMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(
            final HttpMessageNotReadableException e) {
        return toResponseEntity("요청 값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolationException(final Exception e) {
        return toResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleUnexpectedException(final Exception e) {
        log.warn("Internal Server Error", e);
        return toResponseEntity("예상치 못한 예외가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionResponse> toResponseEntity(final String message, final HttpStatus httpStatus) {
        final ExceptionResponse response = new ExceptionResponse(message);
        return ResponseEntity.status(httpStatus)
                .body(response);
    }
}
