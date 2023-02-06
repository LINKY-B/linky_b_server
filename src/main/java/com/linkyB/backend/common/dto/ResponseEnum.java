package com.linkyB.backend.common.dto;

public enum ResponseEnum {
    USER_SIGNUP_SUCCESS("U001","회원가입에 성공하였습니다."),
    USER_LOGIN_SUCCESS("U002","로그인에 성공하였습니다.");

    private final String code;
    private final String message;

    ResponseEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
