package com.linkyB.backend.match.controller;

import com.linkyB.backend.match.dto.MatchDto;
import com.linkyB.backend.match.service.MatchService;
import com.linkyB.backend.user.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
