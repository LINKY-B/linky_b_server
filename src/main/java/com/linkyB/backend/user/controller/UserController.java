package com.linkyB.backend.user.controller;

import com.linkyB.backend.block.dto.BlockDto;
import com.linkyB.backend.block.dto.PatchBlockReq;
import com.linkyB.backend.block.service.BlockService;
import com.linkyB.backend.common.result.ResultResponse;
import com.linkyB.backend.like.dto.LikeDto;
import com.linkyB.backend.like.service.LikeService;
import com.linkyB.backend.report.dto.PostReportReq;
import com.linkyB.backend.report.dto.ReportDto;
import com.linkyB.backend.report.service.ReportService;
import com.linkyB.backend.common.util.SecurityUtils;
import com.linkyB.backend.user.dto.*;
import com.linkyB.backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.linkyB.backend.common.result.ResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final ReportService reportService;
    private final LikeService likeService;
    private final BlockService blockService;
    private final SecurityUtils securityUtils;

    @Operation(summary = "현재 사용자 정보 상세 조회", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M004 - 회원 프로필을 조회하였습니다."),
    })
    @GetMapping
    public ResultResponse<UserDetailResponseDto> findUserById() {
        UserDetailResponseDto response = userService.findUser(securityUtils.getCurrentUserId());
        return new ResultResponse<>(GET_USER_PROFILE_SUCCESS, response);
    }


    @Operation(summary = "다른 사용자 정보 상세 조회", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "404", description = "U001 - 존재 하지 않는 사용자입니다."),
    })
    @GetMapping("/{userId}")
    public ResultResponse<UserDetailResponseDto> findUserById(@PathVariable Long userId) {
        UserDetailResponseDto response = userService.findUser(userId);
        return new ResultResponse<>(GET_USER_PROFILE_SUCCESS, response);
    }

    @Operation(summary = "사용자 신고", description = "사용자를 신고하는 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M017 - 사용자 신고에 성공했습니다."),
    })
    @PostMapping("/report/{userGetReported}")
    public ResultResponse<ReportDto> reportUser(
            @PathVariable("userGetReported") long userGetReported,
            @RequestBody PostReportReq dto) {
        ReportDto response = reportService.reportUser(userGetReported, securityUtils.getCurrentUserId(), dto);
        return new ResultResponse<>(POST_REPORT_SUCCESS, response);
    }


    @Operation(summary = "사용자 좋아요", description = "좋아요 기능")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M018 - 좋아요 등록에 성공했습니다."),
    })
    @PostMapping("/likes/{userGetLikes}")
    public ResultResponse<LikeDto> userLikes(@PathVariable("userGetLikes") long userGetLikes) {
        LikeDto response = likeService.userLikes(securityUtils.getCurrentUserId(), userGetLikes);
        return new ResultResponse<>(POST_LIKE_SUCCESS, response);
    }


    @Operation(summary = "알람 활성화", description = "알람을 활성화합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M019 - 알람을 활성화하는데 성공했습니다."),
    })
    @PatchMapping("/alarm")
    public ResultResponse<UserResponseDto> activeAlarm() {
        UserResponseDto response = userService.activeAlarm(securityUtils.getCurrentUserId());
        return new ResultResponse<>(ACTIVATE_ALARM_SUCCESS, response);
    }


    @Operation(summary = "알람 비활성화", description = "알람을 비활성화합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M020 - 알람을 비활성화하는데 성공했습니다."),
    })
    @PatchMapping("/alarm/inactive")
    public ResultResponse<UserResponseDto> inactiveAlarm() {
        UserResponseDto response = userService.inactiveAlarm(securityUtils.getCurrentUserId());
        return new ResultResponse<>(DEACTIVATE_ALARM_SUCCESS, response);
    }


    @Operation(summary = "사용자 정보 활성화", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M021 - 사용자 정보 활성화에 성공했습니다."),
    })
    @PatchMapping("/active")
    public ResultResponse<UserResponseDto> activeUser() {
        UserResponseDto response = userService.activeUser(securityUtils.getCurrentUserId());
        return new ResultResponse<>(ACTIVATE_USERINFO_SUCCESS, response);
    }


    @Operation(summary = "사용자 정보 비활성화", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M022 - 사용자 정보 비활성화에 성공했습니다."),
    })
    @PatchMapping("/inactive")
    public ResultResponse<UserResponseDto> inactive() {
        UserResponseDto response = userService.inactiveUser(securityUtils.getCurrentUserId());
        return new ResultResponse<>(DEACTIVATE_USERINFO_SUCCESS, response);
    }

    @Operation(summary = "사용자 차단", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M023 - 사용자 차단에 성공했습니다."),
    })
    @PostMapping("/block/{userGetBlocked}")
    public ResultResponse<BlockDto> userBlock(@PathVariable("userGetBlocked") long userGetBlocked) {
        BlockDto response = blockService.userBlock(securityUtils.getCurrentUserId(), userGetBlocked);
        return new ResultResponse<>(BLOCK_USER_SUCCESS, response);
    }


    @Operation(summary = "사용자 차단 해제", description = "차단 해제할 내역의 인덱스를 배열로 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M024 - 사용자 차단 해제에 성공했습니다."),
    })
    @PatchMapping("/block")
    public ResultResponse<UserResponseDto> cancelBlock(@RequestBody PatchBlockReq dto) {
        UserResponseDto response = blockService.cancelBlock(securityUtils.getCurrentUserId(), dto);
        return new ResultResponse<>(FREE_BLOCK_USER_SUCCESS, response);
    }


    @Operation(summary = "차단한 사용자 목록 조회", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M025 - 차단한 사용자 목록 조회에 성공했습니다."),
    })
    @GetMapping("/block")
    public ResultResponse<List<UserListResponseDto>> getBlockedList() {
        List<UserListResponseDto> response = blockService.BlockList(securityUtils.getCurrentUserId());
        return new ResultResponse<>(GET_BLOCK_USER_LIST_SUCCESS, response);
    }


    @Operation(summary = "프로필 수정", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M009 - 회원 프로필을 수정하였습니다."),
    })
    @PatchMapping("/modifyProfile")
    public ResultResponse<UserResponseDto> modifyProfile(@Valid @RequestBody PatchUserReq dto) {
        log.info("[USER Controller] PathUserReq : {}", dto);
        UserResponseDto response = userService.modifyProfile(dto);
        return new ResultResponse<>(EDIT_PROFILE_SUCCESS, response);
    }


    @Operation(summary = "탈퇴", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M026 - 탈퇴에 성공했습니다."),
    })
    @DeleteMapping("/{userId}")
    public ResultResponse<UserResponseDto> deleteUser(@PathVariable("userId") int userId) {
        UserResponseDto response = userService.deleteUser(securityUtils.getCurrentUserId(), userId);
        return new ResultResponse<>(DELETE_USER_SUCCESS, response);
    }
}
