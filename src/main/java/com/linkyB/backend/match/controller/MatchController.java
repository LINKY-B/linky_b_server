package com.linkyB.backend.match.controller;

import com.linkyB.backend.block.dto.BlockDto;
import com.linkyB.backend.config.BaseResponse;
import com.linkyB.backend.match.dto.MatchDto;
import com.linkyB.backend.match.dto.MatchListDto;
import com.linkyB.backend.match.service.MatchService;
import com.linkyB.backend.user.application.AuthorizationService;
import com.linkyB.backend.user.jwt.JwtTokenProvider;
import com.linkyB.backend.user.presentation.dto.UserListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;
    private final AuthorizationService authorizationService;

    // 매칭 시도
    @PostMapping("/{userGetMatched}")
    public BaseResponse<MatchDto> matching(@PathVariable("userGetMatched") long userGetMatched) {
        MatchDto response = matchService.matching(authorizationService.getUserId(), userGetMatched);
        return new BaseResponse<>(response);
    }

    // 매칭 수락
    @PatchMapping("/accept/{userMatching}")
    public BaseResponse<MatchDto> accept(@PathVariable("userMatching") long userMatching) {
        MatchDto response = matchService.accept(authorizationService.getUserId(), userMatching);
        return new BaseResponse<>(response);
    }

    // 매칭 거절
    @PostMapping("/refuse/{userMatching}")
    public BaseResponse<BlockDto> refuse(@PathVariable("userMatching") long userMatching) {
        BlockDto response = matchService.refuse(authorizationService.getUserId(), userMatching);
        return new BaseResponse<>(response);
    }

    // 모든 매칭 수락
    @PatchMapping("/all")
    public BaseResponse<MatchListDto> matckAllOk() {
        MatchListDto response = matchService.all(authorizationService.getUserId());
        return new BaseResponse<>(response);
    }

    // 내가 매칭 시도한 내역 삭제
    @PostMapping("/block/{userGetMatched}")
    public BaseResponse<BlockDto> matchBlock(@PathVariable("userGetMatched") long userGetMatched) {
        BlockDto response = matchService.blockMatch(authorizationService.getUserId(), userGetMatched);
        return new BaseResponse<>(response);
    }

    // 나에게 매칭 시도한 유저 전체 조회
    @GetMapping("/getMatched/all")
    public BaseResponse<List<UserListDto>> GetMatchedList() {
        List<UserListDto> response = matchService.GetMatchedList(authorizationService.getUserId());
        return new BaseResponse<>(response);
    }

    // 내가 매칭 시도한 유저 전체 조회
    @GetMapping("/Matching/all")
    public BaseResponse<List<UserListDto>> MatchingList() {
        List<UserListDto> response = matchService.MatchingList(authorizationService.getUserId());
        return new BaseResponse<>(response);
    }

    // 매칭 내역 리스트 조회 (매칭 홈)
    @GetMapping("/main")
    public BaseResponse<Map<String, List<UserListDto>>> getDoubleList() {
        Map<String, List<UserListDto>> response = new HashMap<>();
        response.put("나에게 연결을 시도한 회원", matchService.homeGetMatchedList(authorizationService.getUserId()));
        response.put("내가 연결을 시도한 회원", matchService.homeMatchingList(authorizationService.getUserId()));

        return new BaseResponse<>(response);
    }
}
