package com.linkyB.backend.match.service;

import com.linkyB.backend.block.converter.Blockconverter;
import com.linkyB.backend.block.entity.Block;
import com.linkyB.backend.block.repository.BlockRepostiory;
import com.linkyB.backend.common.exception.LInkyBussinessException;
import com.linkyB.backend.match.converter.MatchConverter;
import com.linkyB.backend.match.dto.MatchAllOkResDto;
import com.linkyB.backend.match.dto.MatchNoResDto;
import com.linkyB.backend.match.dto.MatchOkResDto;
import com.linkyB.backend.match.dto.MatchingCreateResDto;
import com.linkyB.backend.match.entity.Match;
import com.linkyB.backend.match.entity.status;
import com.linkyB.backend.match.entity.userMatchStatus;
import com.linkyB.backend.match.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchConverter matchConverter;
    private final BlockRepostiory blockRepostiory;
    private final Blockconverter blockconverter;

    @Transactional
    public MatchingCreateResDto matching(final Long userMatching, final Long userGetMatched) {
        final Match match = matchRepository
                .save(matchConverter.ReqCreateMatchDto(userMatching, userGetMatched));
        return matchConverter.ResCreateMatchDto(match);
    }

    @Transactional
    public MatchOkResDto matchOk(Long id) {
        Match match = matchRepository.getById(id);
        match.update(userMatchStatus.ACTIVE);
        return matchConverter.ResMatchOkDto(match);
    }

    @Transactional
    public MatchNoResDto matchNo(Long id) {
        Match match = matchRepository.getById(id);
        match.updateMatch(status.INACTIVE);
        Block block = blockRepostiory.save(blockconverter.ReqMatchNoDto(match.getUserGetMatched(), match.getUserMatching()));
        return blockconverter.ResMatchNoDto(block);
    }

    @Transactional
    public MatchAllOkResDto matchAllOk(Long userGetMatched) {
        matchRepository.updateMatchStatusByUserGetMatched(userGetMatched);
        return matchConverter.ResMatchAllOkDto(userGetMatched);
    }

    @Transactional
    public MatchNoResDto matchDelete(Long id) {
        Match match = matchRepository.getById(id);
        match.updateMatch(status.INACTIVE);
        match.update(userMatchStatus.INACTIVE);
        Block block = blockRepostiory.save(blockconverter.ReqMatchdeleteDto(match.getUserMatching(), match.getUserGetMatched()));
        return blockconverter.ResMatchNoDto(block);
    }
}
