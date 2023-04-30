package com.linkyB.backend.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ResultCode Convention
 * - 도메인 별로 나누어 관리
 * - [동사_목적어_SUCCESS] 형태로 생성
 * - 코드는 도메인명 앞에서부터 1~2글자로 사용
 * - 메시지는 "~~다."로 마무리
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    // Auth
    LOGIN_SUCCESS(200, "A001", "로그인에 성공하였습니다."),
    REISSUE_SUCCESS(200, "A002", "재발급에 성공하였습니다."),
    CONFIRM_EMAIL_FAIL(400, "A003", "이메일 인증을 완료할 수 없습니다."),
    SEND_CONFIRM_EMAIL_SUCCESS(200, "A004", "인증코드 이메일을 전송하였습니다."),
    SEND_RESET_PASSWORD_EMAIL_SUCCESS(200, "A005", "비밀번호 재설정 메일을 전송했습니다."),
    RESET_PASSWORD_SUCCESS(200, "A006", "비밀번호 재설정에 성공했습니다."),
    LOGIN_WITH_CODE_SUCCESS(200, "A007", "비밀번호 재설정 코드로 로그인 했습니다."),
    LOGOUT_SUCCESS(200, "A008", "로그아웃하였습니다."),
    CHECK_RESET_PASSWORD_CODE_GOOD(200, "A009", "올바른 비밀번호 재설정 코드입니다."),
    CHECK_RESET_PASSWORD_CODE_BAD(200, "A010", "올바르지 않은 비밀번호 재설정 코드입니다."),
    LOGOUT_BY_ANOTHER_DEVICE(200, "A011", "다른 기기에 의해 로그아웃되었습니다."),
    GET_LOGIN_DEVICES_SUCCESS(200, "A012", "로그인 한 기기들을 조회하였습니다."),
    LOGOUT_DEVICE_SUCCESS(200, "A013", "해당 기기를 로그아웃 시켰습니다."),
    CONFIRM_EMAIL_SUCCESS(200, "A014", "이메일 인증을 완료했습니다."),

    // Member
    SIGNUP_SUCCESS(200, "M001", "회원가입에 성공하였습니다."),
    GET_USER_PROFILE_SUCCESS(200, "M004", "회원 프로필을 조회하였습니다."),
    UPLOAD_MEMBER_IMAGE_SUCCESS(200, "M006", "회원 이미지를 등록하였습니다."),
    DELETE_MEMBER_IMAGE_SUCCESS(200, "M007", "회원 이미지를 삭제하였습니다."),
    GET_EDIT_PROFILE_SUCCESS(200, "M008", "회원 프로필 수정정보를 조회하였습니다."),
    EDIT_PROFILE_SUCCESS(200, "M009", "회원 프로필을 수정하였습니다."),
    UPDATE_PASSWORD_SUCCESS(200, "M010", "회원 비밀번호를 변경하였습니다."),
    CHECK_USER_NICKNAME_GOOD(200, "M011", "사용가능한 닉네임입니다."),
    CHECK_USER_NICKNAME_BAD(200, "M012", "사용불가능한 닉네임입니다."),
    GET_MENU_MEMBER_SUCCESS(200, "M016", "상단 메뉴 프로필을 조회하였습니다."),
    POST_REPORT_SUCCESS(200, "M017", "사용자 신고에 성공했습니다."),
    POST_LIKE_SUCCESS(200, "M018", "좋아요 등록에 성공했습니다."),
    ACTIVATE_ALARM_SUCCESS(200, "M019", "알람을 활성화하는데 성공했습니다."),
    DEACTIVATE_ALARM_SUCCESS(200, "M020", "알람을 비활성화하는데 성공했습니다."),
    ACTIVATE_USERINFO_SUCCESS(200, "M021", "사용자 정보 활성화에 성공했습니다."),
    DEACTIVATE_USERINFO_SUCCESS(200, "M022", "사용자 정보 비활성화에 성공했습니다."),
    BLOCK_USER_SUCCESS(200, "M023", "사용자 차단에 성공했습니다."),
    FREE_BLOCK_USER_SUCCESS(200, "M024", "사용자 차단 해제에 성공했습니다."),
    GET_BLOCK_USER_LIST_SUCCESS(200, "M025", "차단한 사용자 목록 조회에 성공했습니다."),
    DELETE_USER_SUCCESS(200, "M026", "탈퇴에 성공했습니다."),
    GET_USER_PROFILE_IMAGE_LIST(200, "M027", "프로필 이미지 목록 조회에 성공했습니다."),

    // Home
    GET_HOME_LIST_SUCCESS(200, "H001", "홈 리스트 조회에 성공했습니다"),
    GET_FILTER_SUCCESS(200, "H002", "필터 내용 조회에 성공했습니다"),
    GET_SEARCH_NICKNAME_SUCCESS(200, "H003", "닉네임 검색에 성공했습니다"),

    // Match
    TRY_MATCH_SUCCESS(200, "MA001", "매칭 시도 처리에 성공했습니다"),
    APPROVE_MATCH_SUCCESS(200, "MA002", "매칭 수락 처리에 성공했습니다"),
    REFUSE_MATCH_SUCCESS(200, "MA003", "매칭 거절 처리에 성공했습니다"),
    APPROVE_ALL_MATCH_SUCCESS(200, "MA004", "모든 매칭 수락에 성공했습니다."),
    DELETE_MATCH_HISTORY_SUCCESS(200, "MA005", "매칭 시도한 내역 삭제에 성공했습니다."),
    GET_TRY_MATCH_TO_ME_SUCCESS(200, "MA006", "나에게 매칭 시도한 유저 전체 조회에 성공했습니다."),
    GET_TRY_MATCH_TO_OTHER_SUCCESS(200, "MA007", "내가 매칭 시도한 유저 전체 조회에 성공했습니다."),
    GET_MATCH_HOME_SUCCESS(200, "MA008", "매칭 홈 조회에 성공했습니다."),

    // Chat
    MESSAGE_HANDLING_SUCCESS(200, "C001", "매세지 처리를 성공했습니다."),
    ;


    private final int status;
    private final String code;
    private final String message;

}
