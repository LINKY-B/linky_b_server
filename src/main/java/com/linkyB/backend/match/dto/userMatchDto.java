package com.linkyB.backend.match.dto;

import com.linkyB.backend.match.entity.status;
import com.linkyB.backend.match.entity.userMatchStatus;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class userMatchDto {

    private Long id;
    private Long userGetMatched;
    private Long userMatching;
    private userMatchStatus userMatchStatus;
    private status status;

}
