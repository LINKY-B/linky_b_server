package com.linkyB.backend.match.controller;

import com.linkyB.backend.block.dto.BlockDto;
import com.linkyB.backend.config.BaseResponse;
import com.linkyB.backend.match.dto.MatchDto;
import com.linkyB.backend.match.dto.MatchListDto;
import com.linkyB.backend.match.service.MatchService;
import com.linkyB.backend.user.presentation.dto.UserListDto;
import com.linkyB.backend.user.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;

    // 매칭 시도
    @PostMapping("/{userGetMatched}")
    public BaseResponse<MatchDto> matching(@PathVariable("userGetMatched") long userGetMatched) {
        long userId = SecurityUtil.getCurrentUserId();
        System.out.println(userId);
        MatchDto response = matchService.matching(userId, userGetMatched);
        return new BaseResponse<>(response);
    }

    // 매칭 수락
    @PatchMapping("/accept/{userMatching}")
<<<<<<< HEAD
    public BaseResponse<MatchDto> accept(@PathVariable("userMatching") long userMatching) {
        MatchDto response = matchService.accept(userMatching, SecurityUtil.getCurrentUserId());
        return new BaseResponse<>(response);
=======
    public ResponseEntity<MatchDto> accept(@PathVariable("userMatching") long userMatching) {
        MatchDto response = matchService.accept(userMatching, SecurityUtil.getCurrentUserId());
        return new ResponseEntity<>(response, HttpStatus.OK);
>>>>>>> master
    }

    // 매칭 거절
    @PostMapping("/refuse/{userMatching}")
<<<<<<< HEAD
    public BaseResponse<BlockDto> refuse(@PathVariable("userMatching") long userMatching) {
        BlockDto response = matchService.refuse(SecurityUtil.getCurrentUserId(),userMatching);
        return new BaseResponse<>(response);
=======
    public ResponseEntity<BlockDto> refuse(@PathVariable("userMatching") long userMatching) {
        BlockDto response = matchService.refuse(SecurityUtil.getCurrentUserId(),userMatching);
        return ResponseEntity.ok().body(response);
>>>>>>> master
    }

    // 모든 매칭 수락
    @PatchMapping("/all")
    public BaseResponse<MatchListDto> matckAllOk() {
        MatchListDto response = matchService.all(SecurityUtil.getCurrentUserId());
        return new BaseResponse<>(response);
    }

    // 내가 매칭 시도한 내역 삭제
    @PostMapping("/block/{userGetMatched}")
<<<<<<< HEAD
    public BaseResponse<BlockDto> matchBlock(@PathVariable("userGetMatched") long userGetMatched) {
        BlockDto response = matchService.blockMatch(SecurityUtil.getCurrentUserId(),userGetMatched);
        return new BaseResponse<>(response);
=======
    public ResponseEntity<BlockDto> matchBlock(@PathVariable("userGetMatched") long userGetMatched) {
        BlockDto response = matchService.blockMatch(SecurityUtil.getCurrentUserId(),userGetMatched);
        return ResponseEntity.ok().body(response);
>>>>>>> master
    }

    // 나에게 매칭 시도한 유저 전체 조회
    @GetMapping("/getMatched/all")
    public BaseResponse<List<UserListDto>> GetMatchedList() {
        List<UserListDto> response = matchService.GetMatchedList(SecurityUtil.getCurrentUserId());
        return new BaseResponse<>(response);
    }

    // 내가 매칭 시도한 유저 전체 조회
    @GetMapping("/Matching/all")
    public BaseResponse<List<UserListDto>> MatchingList() {
        List<UserListDto> response = matchService.MatchingList(SecurityUtil.getCurrentUserId());
<<<<<<< HEAD
        return new BaseResponse<>(response);
=======
        return ResponseEntity.ok(response);
>>>>>>> master
    }

    // 매칭 내역 리스트 조회 (매칭 홈)
    @GetMapping("/main")
    public BaseResponse<Map<String, List<UserListDto>>> getDoubleList() {
        Map<String, List<UserListDto>> response = new HashMap<>();
        long userId = SecurityUtil.getCurrentUserId();
        response.put("나에게 연결을 시도한 회원", matchService.homeGetMatchedList(userId));
        response.put("내가 연결을 시도한 회원", matchService.homeMatchingList(userId));

        return new BaseResponse<>(response);
    }
}
