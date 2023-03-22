package com.linkyB.backend.match.controller;

import com.linkyB.backend.block.dto.BlockDto;
import com.linkyB.backend.common.result.ResultResponse;
import com.linkyB.backend.match.dto.MatchDto;
import com.linkyB.backend.match.dto.MatchListDto;
import com.linkyB.backend.match.service.MatchService;
import com.linkyB.backend.common.util.SecurityUtils;
import com.linkyB.backend.user.dto.UserListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.linkyB.backend.common.result.ResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;
    private final SecurityUtils securityUtils;

    // 매칭 시도
    @PostMapping("/{userGetMatched}")
    public ResultResponse<MatchDto> matching(@PathVariable("userGetMatched") long userGetMatched) {
        MatchDto response = matchService.matching(securityUtils.getCurrentUserId(), userGetMatched);
        return new ResultResponse<>(TRY_MATCH_SUCCESS, response);
    }

    // 매칭 수락
    @PatchMapping("/accept/{userMatching}")
    public ResultResponse<MatchDto> accept(@PathVariable("userMatching") long userMatching) {
        MatchDto response = matchService.accept(securityUtils.getCurrentUserId(), userMatching);
        return new ResultResponse<>(APPROVE_MATCH_SUCCESS, response);
    }

    // 매칭 거절
    @PostMapping("/refuse/{userMatching}")
    public ResultResponse<BlockDto> refuse(@PathVariable("userMatching") long userMatching) {
        BlockDto response = matchService.refuse(securityUtils.getCurrentUserId(), userMatching);
        return new ResultResponse<>(REFUSE_MATCH_SUCCESS, response);
    }

    // 모든 매칭 수락
    @PatchMapping("/all")
    public ResultResponse<MatchListDto> matckAllOk() {
        MatchListDto response = matchService.all(securityUtils.getCurrentUserId());
        return new ResultResponse<>(APPROVE_ALL_MATCH_SUCCESS, response);
    }

    // 내가 매칭 시도한 내역 삭제
    @PostMapping("/block/{userGetMatched}")
    public ResultResponse<BlockDto> matchBlock(@PathVariable("userGetMatched") long userGetMatched) {
        BlockDto response = matchService.blockMatch(securityUtils.getCurrentUserId(), userGetMatched);
        return new ResultResponse<>(DELETE_MATCH_HISTORY_SUCCESS, response);
    }

    // 나에게 매칭 시도한 유저 전체 조회
    @GetMapping("/getMatched/all")
    public ResultResponse<List<UserListDto>> GetMatchedList() {
        List<UserListDto> response = matchService.GetMatchedList(securityUtils.getCurrentUserId());
        return new ResultResponse<>(GET_TRY_MATCH_TO_ME_SUCCESS, response);
    }

    // 내가 매칭 시도한 유저 전체 조회
    @GetMapping("/Matching/all")
    public ResultResponse<List<UserListDto>> MatchingList() {
        List<UserListDto> response = matchService.MatchingList(securityUtils.getCurrentUserId());
        return new ResultResponse<>(GET_TRY_MATCH_TO_OTHER_SUCCESS, response);
    }

    // 매칭 내역 리스트 조회 (매칭 홈)
    @GetMapping("/main")
    public ResultResponse<Map<String, List<UserListDto>>> getDoubleList() {
        Map<String, List<UserListDto>> response = new HashMap<>();
        response.put("나에게 연결을 시도한 회원", matchService.homeGetMatchedList(securityUtils.getCurrentUserId()));
        response.put("내가 연결을 시도한 회원", matchService.homeMatchingList(securityUtils.getCurrentUserId()));

        return new ResultResponse<>(GET_MATCH_HOME_SUCCESS, response);
    }
}
