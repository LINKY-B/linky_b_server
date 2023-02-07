package com.linkyB.backend.match.controller;

import com.linkyB.backend.block.dto.BlockDto;
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
    public ResponseEntity<MatchDto> matching(@PathVariable("userGetMatched") long userGetMatched) {
        long userId = SecurityUtil.getCurrentUserId();
        System.out.println(userId);
        MatchDto response = matchService.matching(userId, userGetMatched);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 매칭 수락
    @PatchMapping("/accept/{matchId}")
    public ResponseEntity<MatchDto> accept(@PathVariable("matchId") long matchId) {
        MatchDto response = matchService.accept(matchId, SecurityUtil.getCurrentUserId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 매칭 거절
    @PostMapping("/refuse/{matchId}")
    public ResponseEntity<BlockDto> refuse(@PathVariable("matchId") long matchId) {
        BlockDto response = matchService.refuse(matchId, SecurityUtil.getCurrentUserId());
        return ResponseEntity.ok().body(response);
    }

    // 모든 매칭 수락
    @PatchMapping("/all")
    public ResponseEntity<MatchListDto> matckAllOk() {
        MatchListDto response = matchService.all(SecurityUtil.getCurrentUserId());
        return ResponseEntity.ok().body(response);
    }

    // 내가 매칭 시도한 내역 삭제
    @PostMapping("/block/{matchId}")
    public ResponseEntity<BlockDto> matchBlock(@PathVariable("matchId") long matchId) {
        BlockDto response = matchService.blockMatch(matchId, SecurityUtil.getCurrentUserId());
        return ResponseEntity.ok().body(response);
    }

    // 나에게 매칭 시도한 유저 전체 조회
    @GetMapping("/getMatched/all")
    public ResponseEntity<List<UserListDto>> GetMatchedList() {
        List<UserListDto> response = matchService.GetMatchedList(SecurityUtil.getCurrentUserId());
        return ResponseEntity.ok().body(response);
    }

    // 내가 매칭 시도한 유저 전체 조회
    @GetMapping("/Matching/all")
    public ResponseEntity<List<UserListDto>> MatchingList() {
        List<UserListDto> response = matchService.MatchingList(SecurityUtil.getCurrentUserId());
        return ResponseEntity.ok().body(response);
    }

    // 매칭 내역 리스트 조회 (매칭 홈)
    @GetMapping("/main")
    public ResponseEntity<Map<String, List<UserListDto>>> getDoubleList() {
        Map<String, List<UserListDto>> response = new HashMap<>();
        long userId = SecurityUtil.getCurrentUserId();
        response.put("나에게 연결을 시도한 회원", matchService.homeGetMatchedList(userId));
        response.put("내가 연결을 시도한 회원", matchService.homeMatchingList(userId));

        return ResponseEntity.ok().body(response);
    }
}
