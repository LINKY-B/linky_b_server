package com.linkyB.backend.match.controller;

import com.linkyB.backend.match.dto.MatchOkResDto;
import com.linkyB.backend.match.dto.MatchingCreateResDto;
import com.linkyB.backend.match.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 매칭 수락
    @PatchMapping("/{id}")
    public ResponseEntity<MatchOkResDto> matchOk(@PathVariable("id")Long id) {

        final MatchOkResDto response = matchService.matchOk(id);
        return ResponseEntity.ok().body(response);

    }
}
