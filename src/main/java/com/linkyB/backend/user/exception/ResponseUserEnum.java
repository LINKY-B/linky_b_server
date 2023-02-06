package com.linkyB.backend.user.exception;

import lombok.Getter;

@Getter
public enum ResponseUserEnum {
    USER_SIGNUP_SUCCESS("U001","회원가입에 성공하였습니다.");

    private final String code;
    private final String message;

    ResponseUserEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
