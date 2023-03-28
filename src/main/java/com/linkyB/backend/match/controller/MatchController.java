package com.linkyB.backend.match.controller;

import com.linkyB.backend.block.dto.BlockDto;
import com.linkyB.backend.common.result.ResultResponse;
import com.linkyB.backend.match.dto.MatchDto;
import com.linkyB.backend.match.dto.MatchListDto;
import com.linkyB.backend.match.service.MatchService;
import com.linkyB.backend.common.util.SecurityUtils;
import com.linkyB.backend.user.dto.UserListResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "매칭 시도", description = "{userGetMatched}에게 매칭 시도")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MA001 - 매칭 시도 처리에 성공했습니다."),
    })
    @PostMapping("/{userGetMatched}")
    public ResultResponse<MatchDto> matching(@PathVariable("userGetMatched") long userGetMatched) {
        MatchDto response = matchService.matching(securityUtils.getCurrentUserId(), userGetMatched);
        return new ResultResponse<>(TRY_MATCH_SUCCESS, response);
    }

    @Operation(summary = "매칭 수락", description = "{userMatching}이 시도한 매칭 내역을 수락")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MA002 - 매칭 수락 처리에 성공했습니다"),
            @ApiResponse(responseCode = "403", description = "U004 - 수락 권한이 없습니다."),
    })
    @PatchMapping("/accept/{userMatching}")
    public ResultResponse<MatchDto> accept(@PathVariable("userMatching") long userMatching) {
        MatchDto response = matchService.accept(securityUtils.getCurrentUserId(), userMatching);
        return new ResultResponse<>(APPROVE_MATCH_SUCCESS, response);
    }

    @Operation(summary = "매칭 거절", description = "{userMatching}이 시도한 매칭 내역을 거절")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MA003 - 매칭 거절 처리에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "U004 - 권한이 없습니다."),
    })
    @PostMapping("/refuse/{userMatching}")
    public ResultResponse<BlockDto> refuse(@PathVariable("userMatching") long userMatching) {
        BlockDto response = matchService.refuse(securityUtils.getCurrentUserId(), userMatching);
        return new ResultResponse<>(REFUSE_MATCH_SUCCESS, response);
    }

    @Operation(summary = "모든 매칭 수락", description = "접속한 유저에게 시도된 매칭을 모두 수락")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MA004 - 모든 매칭 수락에 성공했습니다."),
    })
    @PatchMapping("/all")
    public ResultResponse<MatchListDto> matckAllOk() {
        MatchListDto response = matchService.all(securityUtils.getCurrentUserId());
        return new ResultResponse<>(APPROVE_ALL_MATCH_SUCCESS, response);
    }

    @Operation(summary = "내가 매칭 시도한 내역 삭제", description = "{userGetMatched}에 해당하는 내역 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MA005 - 매칭 시도한 내역 삭제 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "U004 - 삭제 권한이 없습니다."),
    })
    @PostMapping("/block/{userGetMatched}")
    public ResultResponse<BlockDto> matchBlock(@PathVariable("userGetMatched") long userGetMatched) {
        BlockDto response = matchService.blockMatch(securityUtils.getCurrentUserId(), userGetMatched);
        return new ResultResponse<>(DELETE_MATCH_HISTORY_SUCCESS, response);
    }

    @Operation(summary = "나에게 매칭 시도한 유저 전체 조회", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MA006 - 나에게 매칭 시도한 유저 전체 조회에 성공했습니다."),
    })
    @GetMapping("/getMatched/all")
    public ResultResponse<List<UserListResponseDto>> GetMatchedList() {
        List<UserListResponseDto> response = matchService.GetMatchedList(securityUtils.getCurrentUserId());
        return new ResultResponse<>(GET_TRY_MATCH_TO_ME_SUCCESS, response);
    }

    @Operation(summary = "내가 매칭 시도한 유저 전체 조회", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MA007 - 내가 매칭 시도한 유저 전체 조회에 성공했습니다."),
    })
    @GetMapping("/Matching/all")
    public ResultResponse<List<UserListResponseDto>> MatchingList() {
        List<UserListResponseDto> response = matchService.MatchingList(securityUtils.getCurrentUserId());
        return new ResultResponse<>(GET_TRY_MATCH_TO_OTHER_SUCCESS, response);
    }

    @Operation(summary = "매칭 홈 조회", description = "나에게 연결을 시도한 회원, 내가 연결을 시도한 회원 리스트 2개 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MA008 - 매칭 홈 조회에 성공했습니다."),
    })
    @GetMapping("/main")
    public ResultResponse<Map<String, List<UserListResponseDto>>> getDoubleList() {
        Map<String, List<UserListResponseDto>> response = new HashMap<>();
        response.put("나에게 연결을 시도한 회원", matchService.homeGetMatchedList(securityUtils.getCurrentUserId()));
        response.put("내가 연결을 시도한 회원", matchService.homeMatchingList(securityUtils.getCurrentUserId()));
        return new ResultResponse<>(GET_MATCH_HOME_SUCCESS, response);
    }
}
