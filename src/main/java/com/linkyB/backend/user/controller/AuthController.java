package com.linkyB.backend.user.controller;

import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;
import com.linkyB.backend.common.result.ResultResponse;
import com.linkyB.backend.common.util.SecurityUtils;
import com.linkyB.backend.user.dto.LoginRequestDto;
import com.linkyB.backend.user.dto.UserPasswordDto;
import com.linkyB.backend.user.dto.UserSignupRequestDto;
import com.linkyB.backend.user.dto.UserSignupResponseDto;
import com.linkyB.backend.user.service.AuthService;
import com.linkyB.backend.user.service.redis.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

import static com.linkyB.backend.common.result.ResultCode.LOGOUT_SUCCESS;
import static com.linkyB.backend.common.result.ResultCode.SIGNUP_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/signup")
    public ResultResponse<UserSignupResponseDto> signup(
            @Valid @RequestPart(value = "UserSignupReq") UserSignupRequestDto userSignupRequestDto,
            @RequestPart(value = "profileImg") MultipartFile profileImg,
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
}
