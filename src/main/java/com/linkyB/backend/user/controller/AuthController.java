package com.linkyB.backend.user.controller;

import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;
import com.linkyB.backend.common.result.ResultResponse;
import com.linkyB.backend.common.util.SecurityUtils;
import com.linkyB.backend.user.dto.*;
import com.linkyB.backend.user.service.AuthService;
import com.linkyB.backend.user.service.S3Uploader;
import com.linkyB.backend.user.service.redis.EmailCodeService;
import com.linkyB.backend.user.service.redis.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static com.linkyB.backend.common.result.ResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailCodeService emailCodeService;
    private final RefreshTokenService refreshTokenService;
    private final S3Uploader s3Uploader;

    @PostMapping("/email/confirm")
    public ResultResponse confirmEmail(@Valid @RequestBody EmailConfirmCodeRequestDto confirmEmailDto) throws MessagingException, UnsupportedEncodingException {
        emailCodeService.sendCodeEmail(confirmEmailDto);
        return ResultResponse.of(SEND_CONFIRM_EMAIL_SUCCESS);
    }

    @PostMapping("/signup")
    public ResultResponse<UserSignupResponseDto> signup(
            @Valid @RequestPart(value = "UserSignupReq") UserSignupRequestDto userSignupRequestDto,
            @RequestPart(value = "profileImg") ProfileImgDto profileImg,
            @RequestPart(value = "schoolImg") MultipartFile schoolImg
    ) throws IOException {
        UserSignupResponseDto response = authService.signup(userSignupRequestDto, profileImg, schoolImg);
        return ResultResponse.of(SIGNUP_SUCCESS, response);
    }


    @PostMapping("/login")
    public ResponseEntity<ResultResponse> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        throw new LinkyBusinessException(ErrorCode.FILTER_MUST_RESPOND);
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue() {
        throw new LinkyBusinessException(ErrorCode.FILTER_MUST_RESPOND);
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<ResultResponse> logoutUser(
            @CookieValue(value = "refreshToken", required = true) String refreshToken,
            HttpServletResponse response
    ) {

        Long id = SecurityUtils.getCurrentUserId();
        refreshTokenService.deleteRefreshTokenByValue(id, refreshToken);

        final Cookie cookie = new Cookie("refreshToken", null);

        cookie.setMaxAge(0);

        // cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);

        return ResponseEntity.ok(ResultResponse.of(LOGOUT_SUCCESS));
    }


    @PostMapping("/password")
    public ResponseEntity<UserPasswordDto> updatePassword(@RequestBody UserPasswordDto passwordRequestDto) {
        return ResponseEntity.ok(authService.updatePassword(passwordRequestDto));
    }

    @PostMapping("/s3")
    public ResponseEntity<?> uploadImage(@RequestPart(value = "profileImg") MultipartFile profileImg) throws IOException {
        String uploadUrl = s3Uploader.upload(profileImg, "images/profileImg/");
        return ResponseEntity.ok(uploadUrl);
    }
}
