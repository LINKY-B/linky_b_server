package com.linkyB.backend.common.dto;

import com.linkyB.backend.user.exception.ResponseUserEnum;
import lombok.Getter;

@Getter
public class Response<T> {
    private final String code;
    private final String message;
    private final T data;

    public Response(ResponseUserEnum responseUserEnum, T data) {
        this(responseUserEnum.getCode(), responseUserEnum.getMessage(), data);
    }

    public Response(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
