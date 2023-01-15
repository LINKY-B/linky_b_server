package com.linkyB.backend.match.service;

import com.linkyB.backend.match.converter.MatchConverter;
import com.linkyB.backend.match.dto.MatchingCreateResDto;
import com.linkyB.backend.match.entity.Match;
import com.linkyB.backend.match.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchConverter matchConverter;

    @Transactional
    public MatchingCreateResDto matching(final Long userMatching, final Long userGetMatched) {
        final Match match = matchRepository
                .save(matchConverter.ReqCreateMatchDto(userMatching, userGetMatched));

        return matchConverter.ResCreateMatchDto(match);
    }
}
