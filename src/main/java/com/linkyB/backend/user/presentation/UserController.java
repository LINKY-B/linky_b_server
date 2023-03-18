package com.linkyB.backend.user.presentation;

import com.linkyB.backend.block.dto.BlockDto;
import com.linkyB.backend.block.dto.PatchBlockReq;
import com.linkyB.backend.block.service.BlockService;
import com.linkyB.backend.config.BaseResponse;
import com.linkyB.backend.like.dto.LikeDto;
import com.linkyB.backend.like.service.LikeService;
import com.linkyB.backend.report.dto.PostReportReq;
import com.linkyB.backend.report.dto.ReportDto;
import com.linkyB.backend.report.service.ReportService;
import com.linkyB.backend.user.application.AuthorizationService;
import com.linkyB.backend.user.application.UserService;

import com.linkyB.backend.user.presentation.dto.*;
import lombok.RequiredArgsConstructor;
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
    private final AuthorizationService authorizationService;

    // 유저 정보 상세 조회 (유저 인덱스 이용)
    @GetMapping("")
    public BaseResponse<UserDetailDto> findUserById() {
        UserDetailDto response = userService.findUser(authorizationService.getUserId());
        return new BaseResponse<>(response);
    }

    // 유저 정보 상세 조회 (유저 인덱스 이용)
    @GetMapping("/{userId}")
    public BaseResponse<UserDetailDto> findUserById(@PathVariable Long userId) {
        UserDetailDto response = userService.findUser(userId);
        return new BaseResponse<>(response);
    }

    // 유저 신고 등록
    @PostMapping("/report/{userGetReported}")
    public BaseResponse<ReportDto> reportUser(@PathVariable("userGetReported")long userGetReported,
                                              @RequestBody PostReportReq dto) {
        ReportDto response = reportService.reportUser(userGetReported, authorizationService.getUserId(), dto);
        return new BaseResponse<>(response);
    }

    // 유저 좋아요 등록
    @PostMapping("/likes/{userGetLikes}")
    public BaseResponse<LikeDto> userLikes(@PathVariable("userGetLikes")long userGetLikes) {
        LikeDto response = likeService.userLikes(authorizationService.getUserId(), userGetLikes);
        return new BaseResponse<>(response);
    }

    // 알림 활성화
    @PatchMapping("/alaram")
    public BaseResponse<UserDto> activeAlaram() {
        UserDto response = userService.activeAlaram(authorizationService.getUserId());
        return new BaseResponse<>(response);
    }

    // 알림 비활성화
    @PatchMapping("/alaram/inactive")
    public BaseResponse<UserDto> inactiveAlaram() {
        UserDto response = userService.inactiveAlaram(authorizationService.getUserId());
        return new BaseResponse<>(response);
    }

    // 정보 활성화
    @PatchMapping("/active")
    public BaseResponse<UserDto> activeUser() {
        UserDto response = userService.activeUser(authorizationService.getUserId());
        return new BaseResponse<>(response);
    }

    // 정보 비활성화
    @PatchMapping("/inactive")
    public BaseResponse<UserDto> inactive() {
        UserDto response = userService.inactiveUser(authorizationService.getUserId());
        return new BaseResponse<>(response);
    }

    // 유저 차단
    @PostMapping("/block/{userGetBlocked}")
    public BaseResponse<BlockDto> UserBlock(@PathVariable("userGetBlocked") long userGetBlocked) {
        BlockDto response = blockService.userBlock(authorizationService.getUserId(), userGetBlocked);
        return new BaseResponse<>(response);
    }

    // 차단 해제
    @PatchMapping("/block")
    public BaseResponse<UserDto> cancleBlock(@RequestBody PatchBlockReq dto) {
        UserDto response = blockService.cancleBlock(authorizationService.getUserId(), dto);
        return new BaseResponse<>(response);
    }

    // 차단 유저 리스트 조회
    @GetMapping("/block")
    public BaseResponse<List<UserListDto>> GetBlockedList() {
        List<UserListDto> response = blockService.BlockList(authorizationService.getUserId());
        return new BaseResponse<>(response);
    }

    // 유저 프로필 수정
    @PatchMapping("/modifyProfile")
    public BaseResponse<UserDto> modifyProfile(@RequestPart (value = "PatchUserReq") PatchUserReq dto,
                                                 @RequestPart(value = "profileImg") MultipartFile multipartFile) throws IOException {
        UserDto response = userService.modifyProfile(authorizationService.getUserId(), dto, multipartFile);
        return new BaseResponse<>(response);
    }

    // 유저 탈퇴
    @DeleteMapping("/{userId}")
    public BaseResponse<UserDto> deleteUser(@PathVariable("userId")int userId) {
        UserDto response = userService.deleteUser(authorizationService.getUserId(),userId);
        return new BaseResponse<>(response);
    }
}
