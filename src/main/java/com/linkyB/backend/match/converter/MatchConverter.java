package com.linkyB.backend.match.converter;

import com.linkyB.backend.match.dto.MatchListDto;
import com.linkyB.backend.match.entity.Match;
import com.linkyB.backend.match.entity.MatchStatus;
import com.linkyB.backend.match.entity.status;
import com.linkyB.backend.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class MatchConverter {
    public Match tryMatching(final User userMatching, final User userGetMatched) {
        return Match.builder()
                .userMatching(userMatching)
                .userGetMatched(userGetMatched)
                .userMatchStatus(MatchStatus.INACTIVE)
                .status(status.ACTIVE)
                .build();
    }

    public MatchListDto MatchAllokResponseDto(long userId) {
        return MatchListDto.builder()
                .userGetMatched(userId)
                .build();
    }
}
