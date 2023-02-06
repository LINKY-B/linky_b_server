package com.linkyB.backend.user.presentation;

import com.linkyB.backend.user.application.AuthService;
import com.linkyB.backend.user.presentation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponseDto> signup(@Valid @RequestBody UserSignupRequestDto userSignupRequestDto) {
        return ResponseEntity.ok(authService.signup(userSignupRequestDto));
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
