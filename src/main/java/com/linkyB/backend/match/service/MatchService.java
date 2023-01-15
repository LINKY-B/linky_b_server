package com.linkyB.backend.match.service;

import com.linkyB.backend.match.dto.MatchOkResDto;
import com.linkyB.backend.match.dto.MatchingCreateResDto;
import org.springframework.stereotype.Service;

public interface MatchService {
    MatchingCreateResDto matching(Long userMatching, Long userGetMatched);
    MatchOkResDto matchOk(Long id);
}
