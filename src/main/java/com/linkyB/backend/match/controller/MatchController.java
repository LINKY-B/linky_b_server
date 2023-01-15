package com.linkyB.backend.match.controller;

import com.linkyB.backend.match.dto.MatchingCreateResDto;
import com.linkyB.backend.match.service.MatchService;
import com.linkyB.backend.match.service.MatchServiceImpl;
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
    @PostMapping("/{userMatching}/{userGetMatched}")
    public ResponseEntity<MatchingCreateResDto> matching(@PathVariable("userMatching")final Long userMatching,
                         @PathVariable("userGetMatched")final Long userGetMatched) {
        final MatchingCreateResDto response = matchService.matching(userMatching, userGetMatched);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
