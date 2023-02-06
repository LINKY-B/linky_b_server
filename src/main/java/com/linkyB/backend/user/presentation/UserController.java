package com.linkyB.backend.user.presentation;

import com.linkyB.backend.user.application.UserService;
import com.linkyB.backend.user.presentation.dto.UserDetailDto;
import com.linkyB.backend.user.presentation.dto.UserSignupResponseDto;
import com.linkyB.backend.user.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/auth")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserSignupResponseDto> findUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findUserById(SecurityUtil.getCurrentUserId()));
    }


    // 유저 상세 정보 조회
    @GetMapping("")
    public ResponseEntity<UserDetailDto> findUser() {
        UserDetailDto response = userService.findUser(SecurityUtil.getCurrentUserId());
        return ResponseEntity.ok().body(response);
    }

}