package com.linkyB.backend.user.presentation;

import com.linkyB.backend.block.dto.BlockDto;
import com.linkyB.backend.block.dto.PatchBlockReq;
import com.linkyB.backend.block.service.BlockService;
import com.linkyB.backend.like.dto.LikeDto;
import com.linkyB.backend.like.service.LikeService;
import com.linkyB.backend.report.dto.PostReportReq;
import com.linkyB.backend.report.dto.ReportDto;
import com.linkyB.backend.report.entity.Report;
import com.linkyB.backend.report.service.ReportService;
import com.linkyB.backend.user.application.UserService;
import com.linkyB.backend.user.presentation.dto.*;
import com.linkyB.backend.user.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ReportService reportService;
    private final LikeService likeService;
    private final BlockService blockService;

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

    @PatchMapping("/active")
    public ResponseEntity<UserDto> activeUser() {
        UserDto response = userService.activeUser(SecurityUtil.getCurrentUserId());
        return ResponseEntity.ok().body(response);
    }

    // 정보 비활성화
    @PatchMapping("/inactive")
    public ResponseEntity<UserDto> inactiveUser() {
        UserDto response = userService.inactiveUser(SecurityUtil.getCurrentUserId());
        return ResponseEntity.ok().body(response);
    }

    // 유저 차단
    @PostMapping("/block/{userGetBlocked}")
    public ResponseEntity<BlockDto> UserBlock(@PathVariable("userGetBlocked") long userGetBlocked) {
        long userId = SecurityUtil.getCurrentUserId();
        BlockDto response = blockService.userBlock(userId, userGetBlocked);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 차단 해제
    @PatchMapping("/block")
    public ResponseEntity<UserDto> cancleBlock(@RequestBody PatchBlockReq dto) {
        UserDto response = blockService.cancleBlock(SecurityUtil.getCurrentUserId(), dto);
        return ResponseEntity.ok().body(response);
    }

    // 차단 유저 리스트 조회
    @GetMapping("/block")
    public ResponseEntity<List<UserListDto>> GetBlockedList() {
        List<UserListDto> response = blockService.BlockList(SecurityUtil.getCurrentUserId());
        return ResponseEntity.ok().body(response);
    }

    // 유저 프로필 수정
    @PatchMapping("/modifyProfile")
    public ResponseEntity<UserDto> modifyProfile(@RequestPart (value = "PatchUserReq") PatchUserReq dto,
                                                 @RequestPart(value = "profileImg") MultipartFile multipartFile) throws IOException {
        UserDto response = userService.modifyProfile(SecurityUtil.getCurrentUserId(), dto, multipartFile);
        return ResponseEntity.ok().body(response);
    }
}
