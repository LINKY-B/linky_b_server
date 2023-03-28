package com.linkyB.backend.match.dto;

import com.linkyB.backend.match.entity.Match;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class MatchDto {
    private long matchId;

    public static MatchDto of(Match match) {
        return MatchDto.builder()
                .matchId(match.getMatchId())
                .build();
    }
}
