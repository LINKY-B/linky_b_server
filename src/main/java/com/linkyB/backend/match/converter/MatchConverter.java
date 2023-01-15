package com.linkyB.backend.match.converter;

import com.linkyB.backend.match.dto.MatchingCreateResDto;
import com.linkyB.backend.match.dto.userMatchDto;
import com.linkyB.backend.match.entity.Match;
import com.linkyB.backend.match.entity.userMatchStatus;
import org.springframework.stereotype.Component;

@Component
public class MatchConverter {

    public Match ReqCreateMatchDto(final Long userMatching, final Long userGetMatched) {
        return Match.builder()
                .userMatching(userMatching)
                .userGetMatched(userGetMatched)
                .userMatchStatus(userMatchStatus.INACTIVE)
                .build();
    }

    public MatchingCreateResDto ResCreateMatchDto(Match match) {
        return MatchingCreateResDto.builder()
                .id(match.getId())
                .build();
    }
}
