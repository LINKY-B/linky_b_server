package com.linkyB.backend.user.presentation;

import com.linkyB.backend.report.dto.PostReportReq;
import com.linkyB.backend.report.dto.ReportDto;
import com.linkyB.backend.report.entity.Report;
import com.linkyB.backend.report.service.ReportService;
import com.linkyB.backend.user.application.UserService;
import com.linkyB.backend.user.presentation.dto.UserDetailDto;
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

}
