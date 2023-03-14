package com.linkyB.backend.match.controller;

import com.linkyB.backend.block.dto.BlockDto;
import com.linkyB.backend.config.BaseResponse;
import com.linkyB.backend.match.dto.MatchDto;
import com.linkyB.backend.match.dto.MatchListDto;
import com.linkyB.backend.match.service.MatchService;
import com.linkyB.backend.user.jwt.JwtTokenProvider;
import com.linkyB.backend.user.presentation.dto.UserListDto;
import com.linkyB.backend.user.util.SecurityUtil;
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
    private final JwtTokenProvider jwtTokenProvider;

    // 매칭 시도
    @PostMapping("/{userGetMatched}")
    public BaseResponse<MatchDto> matching(@PathVariable("userGetMatched") long userGetMatched) {
        long TokenUser = jwtTokenProvider.getUser();

        MatchDto response = matchService.matching(TokenUser, userGetMatched);
        return new BaseResponse<>(response);
    }

    // 매칭 수락
    @PatchMapping("/accept/{userMatching}")
    public BaseResponse<MatchDto> accept(@PathVariable("userMatching") long userMatching) {
        long TokenUser = jwtTokenProvider.getUser();
        MatchDto response = matchService.accept(TokenUser, userMatching);
        return new BaseResponse<>(response);
    }

    // 매칭 거절
    @PostMapping("/refuse/{userMatching}")
    public BaseResponse<BlockDto> refuse(@PathVariable("userMatching") long userMatching) {
        long TokenUser = jwtTokenProvider.getUser();
        BlockDto response = matchService.refuse(TokenUser,userMatching);
        return new BaseResponse<>(response);
    }

    // 모든 매칭 수락
    @PatchMapping("/all")
    public BaseResponse<MatchListDto> matckAllOk() {
        long TokenUser = jwtTokenProvider.getUser();
        MatchListDto response = matchService.all(TokenUser);
        return new BaseResponse<>(response);
    }

    // 내가 매칭 시도한 내역 삭제
    @PostMapping("/block/{userGetMatched}")
    public BaseResponse<BlockDto> matchBlock(@PathVariable("userGetMatched") long userGetMatched) {
        long TokenUser = jwtTokenProvider.getUser();
        BlockDto response = matchService.blockMatch(TokenUser,userGetMatched);
        return new BaseResponse<>(response);
    }

    // 나에게 매칭 시도한 유저 전체 조회
    @GetMapping("/getMatched/all")
    public BaseResponse<List<UserListDto>> GetMatchedList() {
        long TokenUser = jwtTokenProvider.getUser();
        List<UserListDto> response = matchService.GetMatchedList(TokenUser);
        return new BaseResponse<>(response);
    }

    // 내가 매칭 시도한 유저 전체 조회
    @GetMapping("/Matching/all")
    public BaseResponse<List<UserListDto>> MatchingList() {
        long TokenUser = jwtTokenProvider.getUser();
        List<UserListDto> response = matchService.MatchingList(TokenUser);
        return new BaseResponse<>(response);
    }

    // 매칭 내역 리스트 조회 (매칭 홈)
    @GetMapping("/main")
    public BaseResponse<Map<String, List<UserListDto>>> getDoubleList() {
        Map<String, List<UserListDto>> response = new HashMap<>();
        long TokenUser = jwtTokenProvider.getUser();
        response.put("나에게 연결을 시도한 회원", matchService.homeGetMatchedList(TokenUser));
        response.put("내가 연결을 시도한 회원", matchService.homeMatchingList(TokenUser));

        return new BaseResponse<>(response);
    }
}
