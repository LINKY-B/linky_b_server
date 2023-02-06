package com.linkyB.backend.user.presentation;

import com.linkyB.backend.like.dto.LikeDto;
import com.linkyB.backend.like.service.LikeService;
import com.linkyB.backend.report.dto.PostReportReq;
import com.linkyB.backend.report.dto.ReportDto;
import com.linkyB.backend.report.entity.Report;
import com.linkyB.backend.report.service.ReportService;
import com.linkyB.backend.user.application.UserService;
import com.linkyB.backend.user.presentation.dto.UserDetailDto;
import com.linkyB.backend.user.presentation.dto.UserDto;
import com.linkyB.backend.user.presentation.dto.UserSignupResponseDto;
import com.linkyB.backend.user.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ReportService reportService;
    private final LikeService likeService;

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

    // 유저 신고 등록
    @PostMapping("/report/{userGetReported}")
    public ResponseEntity<ReportDto> reportUser(@PathVariable("userGetReported")long userGetReported,
                                                @RequestBody PostReportReq dto) {
        long userId = SecurityUtil.getCurrentUserId();
        ReportDto response = reportService.reportUser(userGetReported, userId, dto);
        return ResponseEntity.ok().body(response);
    }

    // 유저 좋아요 등록
    @PostMapping("likes/{userGetLikes}")
    public ResponseEntity<LikeDto> userLikes(@PathVariable("userGetLikes")long userGetLikes) {
        long userId = SecurityUtil.getCurrentUserId();
        LikeDto response = likeService.userLikes(userGetLikes, userId);
        return ResponseEntity.ok().body(response);
    }

    // 알림 활성화
    @PatchMapping("/alaram")
    public ResponseEntity<UserDto> activeAlaram() {
        UserDto response = userService.activeAlaram(SecurityUtil.getCurrentUserId());
        return ResponseEntity.ok().body(response);
    }

    // 알림 비활성화
    @PatchMapping("/alaram/inactive")
    public ResponseEntity<UserDto> inactiveAlaram() {
        UserDto response = userService.inactiveAlaram(SecurityUtil.getCurrentUserId());
        return ResponseEntity.ok().body(response);
    }
}
