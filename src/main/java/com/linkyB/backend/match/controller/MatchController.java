package com.linkyB.backend.match.controller;

import com.linkyB.backend.match.dto.MatchAllOkResDto;
import com.linkyB.backend.match.dto.MatchNoResDto;
import com.linkyB.backend.match.dto.MatchOkResDto;
import com.linkyB.backend.match.dto.MatchingCreateResDto;
import com.linkyB.backend.match.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 매칭 거절
    @PostMapping("/{id}")
    public ResponseEntity<MatchNoResDto> matchNo(@PathVariable("id")Long id) {
        final MatchNoResDto response = matchService.matchNo(id);
        return ResponseEntity.ok().body(response);
    }

    // 모든 매칭 수락
    @PatchMapping("/{userGetMatched}/all")
    public ResponseEntity<MatchAllOkResDto> matckAllOk(@PathVariable("userGetMatched")Long userGetMatched) {
        final MatchAllOkResDto response = matchService.matchAllOk(userGetMatched);
        return ResponseEntity.ok().body(response);
    }
}
