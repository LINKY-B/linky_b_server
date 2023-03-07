package com.linkyB.backend.user.presentation;

import com.linkyB.backend.user.application.AuthService;
import com.linkyB.backend.user.presentation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponseDto> signup(@Valid @RequestPart(value = "UserSignupReq") UserSignupRequestDto userSignupRequestDto,
                                                        @RequestPart(value = "profileImg")MultipartFile userProfileImg,
                                                        @RequestPart(value = "schoolImg")MultipartFile userSchoolImg) throws IOException {
        return ResponseEntity.ok(authService.signup(userSignupRequestDto,userProfileImg, userSchoolImg));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserLoginDto userLoginDto) {
        return ResponseEntity.ok(authService.login(userLoginDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @PostMapping("/password")
    public ResponseEntity<UserPasswordDto> updatePassword(@RequestBody UserPasswordDto passwordRequestDto) {
        return ResponseEntity.ok(authService.updatePassword(passwordRequestDto));
    }
}
