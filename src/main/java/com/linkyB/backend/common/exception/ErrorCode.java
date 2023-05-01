package com.linkyB.backend.common.exception;

/**
 * ErrorCode Convention
 * - 도메인 별로 나누어 관리
 * - [주체_이유] 형태로 생성
 * - 코드는 도메인명 앞에서부터 1~2글자로 사용
 * - 메시지는 "~~다."로 마무리
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Global
    INTERNAL_SERVER_ERROR(500, "G001", "내부 서버 오류입니다."),
    METHOD_NOT_ALLOWED(405, "G002", "허용되지 않은 HTTP method입니다."),
    INPUT_VALUE_INVALID(400, "G003", "유효하지 않은 입력입니다."),
    INPUT_TYPE_INVALID(400, "G004", "입력 타입이 유효하지 않습니다."),
    HTTP_MESSAGE_NOT_READABLE(400, "G005", "request message body가 없거나, 값 타입이 올바르지 않습니다."),
    HTTP_HEADER_INVALID(400, "G006", "request header가 유효하지 않습니다."),
    IMAGE_TYPE_NOT_SUPPORTED(400, "G007", "지원하지 않는 이미지 타입입니다."),
    FILE_CONVERT_FAIL(500, "G008", "변환할 수 없는 파일입니다."),
    ENTITY_TYPE_INVALID(500, "G009", "올바르지 않은 entity type 입니다."),
    FILTER_MUST_RESPOND(500, "G010", "필터에서 처리해야 할 요청이 Controller에 접근하였습니다."),

    // Authorization
    JWT_INVALID(400, "A001", "잘못된 인증 토큰입니다."),
    JWT_EXPIRED(400, "A002", "만료된 인증 토큰입니다."),
    JWT_NOT_EXPIRED(401, "A003", "만료되지 않은 인증 토큰입니다."),
    GET_USER_FAILED(500, "A004", "로그인 된 사용자를 가져오지 못했습니다. 로그인 상태를 확인해주세요."),
    UPDATE_PASSWORD_FAILED(400, "A005", "비밀번호 변경에 실패했습니다. 이전 비밀번호와 동일합니다."),
    CONFIRM_CODE_NOT_VALID(401, "A006", "인증 코드가 만료되었거나 일치하지 않습니다."),
    AUTHENTICATION_FAIL(401, "A007", "로그인이 필요한 화면입니다."),
    AUTHORITY_INVALID(403, "A008", "비밀번호가 일치하지 않거나 권한이 없습니다."),
    EMAIL_NOT_CONFIRMED(400, "A009", "인증 이메일 전송을 먼저 해야합니다."),
    PASSWORD_RESET_FAIL(400, "A010", "잘못되거나 만료된 코드입니다."),

    // User
    USER_NOT_FOUND(404, "U001", "존재 하지 않는 사용자입니다."),
    EMAIL_ALREADY_EXIST(400, "U002", "이미 존재하는 이메일입니다."),
    ACCOUNT_MISMATCH(401, "U003", "계정 정보가 일치하지 않습니다."),
    BLOCK_ALREADY_EXIST(400, "U004", "이미 차단한 유저입니다."),
    UNBLOCK_FAIL(400, "U005", "차단하지 않은 유저는 차단해제 할 수 없습니다."),
    BLOCK_MYSELF_FAIL(400, "U006", "자기 자신을 차단 할 수 없습니다."),
    UNBLOCK_MYSELF_FAIL(400, "U007", "자기 자신을 차단해제 할 수 없습니다."),
    PASSWORD_EQUAL_WITH_OLD(400, "U008", "기존 비밀번호와 동일하게 변경할 수 없습니다."),
    LOGOUT_BY_ANOTHER(401, "U009", "다른 기기에 의해 로그아웃되었습니다."),
    PROFILE_IMAGE_NOT_FOUND(404, "U010", "존재하지 않는 프로필 이미지입니다."),
    NICKNAME_ALREADY_EXIST(400, "U011", "이미 존재하는 닉네임입니다."),

    // Like

    // Match
    MATCH_NOT_FOUND(404, "M001", "해당 연결내역이 존재하지 않습니다."),

    //Chat
    EMPTY_FIELD_FOUND(422 , "C001", "메세지에 비어있는 필드가 존재합니다."),
    MESSAGE_HANDLE_FAIL(500, "C002", "채팅 메세지 처리에 실패했습니다."),
    MESSAGE_SAVE_FAIL(500, "C003", "채팅 메세지 저장에 실패했습니다."),
    CHATROOM_ACCESS_NOT_ALLOW(403 , "C004", "해당 채팅방에 속한 사용자가 아닙니다."),
    ;

    private final int status;
    private final String code;
    private final String message;

}
