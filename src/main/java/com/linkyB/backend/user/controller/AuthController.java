package com.linkyB.backend.user.controller;

import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;
import com.linkyB.backend.common.result.ResultResponse;
import com.linkyB.backend.common.util.SecurityUtils;
import com.linkyB.backend.user.dto.*;
import com.linkyB.backend.user.service.AuthService;
import com.linkyB.backend.user.service.redis.EmailCodeService;
import com.linkyB.backend.user.service.redis.RefreshTokenService;
import com.linkyB.backend.user.util.UserProfileUrls;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

import static com.linkyB.backend.common.result.ResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final EmailCodeService emailCodeService;
    private final RefreshTokenService refreshTokenService;


    @Operation(summary = "회원가입 인증 이메일 발송", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "A004 - 인증코드 이메일을 전송하였습니다."),
    })
    @PostMapping("/signup/send-email")
    public ResultResponse sendEmailForSignup(@Valid @RequestBody EmailSendConfirmCodeRequestDto confirmEmailDto) throws MessagingException, UnsupportedEncodingException {
        emailCodeService.sendCodeEmail(confirmEmailDto);
        return ResultResponse.of(SEND_CONFIRM_EMAIL_SUCCESS);
    }


    @Operation(summary = "회원가입 인증번호 확인", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "A014 - 이메일 인증을 완료했습니다."),
            @ApiResponse(responseCode = "401", description = "U015 - 인증 코드가 만료되었거나 일치하지 않습니다."),
    })
    @PostMapping("/signup/confirm-email")
    public ResultResponse confirmEmailForSignup(@Valid @RequestBody EmailConfirmCodeRequestDto confirmEmailDto) {
        return authService.isValidEmailToken(confirmEmailDto);
    }


    @Operation(summary = "회원 가입시 사용할 수 있는 프로필 이미지 목록 조회", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M027 - 프로필 이미지 목록 조회에 성공했습니다."),
    })
    @GetMapping("/profile-images")
    public ResultResponse<List<String>> signup() {
        List<String> profileUrls = UserProfileUrls.PROFILE_IMAGE_URLS.stream().collect(Collectors.toList());
        return ResultResponse.of(GET_USER_PROFILE_IMAGE_LIST, profileUrls);
    }


    @Operation(summary = "닉네임 중복 확인", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M011 - 사용가능한 닉네임입니다."),
            @ApiResponse(responseCode = "200", description = "M012 - 사용 불가능한 닉네임입니다."),
    })
    @PostMapping("/check-nickname")
    public ResultResponse checkUniqueNickName(@Valid CheckUserNickNameRequestDto nickNameRequestDto) {
        log.info("is unique? target : {}", nickNameRequestDto.getNickName());
        boolean isUnique = authService.isUniqueNickName(nickNameRequestDto.getNickName());
        if (isUnique) {
            return ResultResponse.of(CHECK_USER_NICKNAME_GOOD);
        }
        return ResultResponse.of(CHECK_USER_NICKNAME_BAD);
    }


    @Operation(summary = "회원 가입 처리", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M001 - 회원가입에 성공하였습니다."),
    })
    @PostMapping("/signup")
    public ResultResponse<UserSignupResponseDto> signup(
            @Valid @RequestPart(value = "UserSignupReq") UserSignupRequestDto userSignupRequestDto,
            @RequestPart(value = "schoolImg") MultipartFile schoolImg
    ) throws IOException {
        UserSignupResponseDto response = authService.signup(userSignupRequestDto, schoolImg);
        return ResultResponse.of(SIGNUP_SUCCESS, response);
    }


    @PostMapping("/login")
    public ResponseEntity<ResultResponse> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        throw new LinkyBusinessException(ErrorCode.FILTER_MUST_RESPOND);
    }


    @PostMapping("/reissue")
    public ResponseEntity<?> reissue() {
        throw new LinkyBusinessException(ErrorCode.FILTER_MUST_RESPOND);
    }


    @Operation(summary = "로그아웃", description = "즉시 로그아웃된 효과를 내기 위해서는 accessToken을 request header에서 제거해주세요!")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "A008 - 로그아웃하였습니다."),
    })
    @PostMapping("/logout")
    public ResultResponse logoutUser(@CookieValue(value = "refreshToken") String refreshToken, HttpServletResponse response) {
        final Long id = SecurityUtils.getCurrentUserId();
        refreshTokenService.deleteRefreshTokenByValue(id, refreshToken);

        final Cookie cookie = new Cookie("refreshToken", null);

        cookie.setMaxAge(0);

        // cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);

        return ResultResponse.of(LOGOUT_SUCCESS);
    }


    @Operation(summary = "비밀번호 변경을 위한 이메일 전송", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "A005 - 비밀번호 재설정 이메일을 전송했습니다."),
    })
    @PostMapping("/reset-password/send-email")
    public ResultResponse confirmEmailForPassword(@Valid ResetPasswordSendEmailRequestDto dto) throws MessagingException, UnsupportedEncodingException {
        emailCodeService.sendCodeEmail(dto.getEmail());
        return ResultResponse.of(SEND_RESET_PASSWORD_EMAIL_SUCCESS);
    }


    @Operation(summary = "비밀번호 변경", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "A006 - 비밀번호 재설정에 성공했습니다."),
            @ApiResponse(responseCode = "200", description = "U015 - 인증코드가 만료되었거나 일치하지 않습니다"),
            @ApiResponse(responseCode = "400", description = "A005 - 비밀번호 변경에 실패했습니다. 이전 비밀번호와 동일합니다."),
    })
    @PostMapping("/reset-password")
    public ResultResponse updatePassword(@Valid @RequestBody ResetPasswordRequestDto passwordRequestDto) {
        authService.updatePassword(passwordRequestDto);
        return ResultResponse.of(RESET_PASSWORD_SUCCESS);
    }
}
