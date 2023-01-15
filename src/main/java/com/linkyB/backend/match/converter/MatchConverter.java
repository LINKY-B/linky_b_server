package com.linkyB.backend.match.converter;

import com.linkyB.backend.match.dto.MatchAllOkResDto;
import com.linkyB.backend.match.dto.MatchOkResDto;
import com.linkyB.backend.match.dto.MatchingCreateResDto;
import com.linkyB.backend.match.dto.userMatchDto;
import com.linkyB.backend.match.entity.Match;
import com.linkyB.backend.match.entity.status;
import com.linkyB.backend.match.entity.userMatchStatus;
import org.springframework.stereotype.Component;

@Component
public class MatchConverter {

    public Match ReqCreateMatchDto(final Long userMatching, final Long userGetMatched) {
        return Match.builder()
                .userMatching(userMatching)
                .userGetMatched(userGetMatched)
                .userMatchStatus(userMatchStatus.INACTIVE)
                .status(status.ACTIVE)
                .build();
    }

    public MatchingCreateResDto ResCreateMatchDto(Match match) {
        return MatchingCreateResDto.builder()
                .id(match.getId())
                .build();
    }

    public MatchOkResDto ResMatchOkDto(Match match) {
        return MatchOkResDto.builder()
                .id(match.getId())
                .build();
    }

    public MatchAllOkResDto ResMatchAllOkDto(Long userGetMatched) {
        return MatchAllOkResDto.builder()
                .userGetMatched(userGetMatched)
                .build();
    }


}
