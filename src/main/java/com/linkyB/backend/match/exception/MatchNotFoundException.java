package com.linkyB.backend.match.exception;

import com.linkyB.backend.common.exception.LinkyBusinessException;

import static com.linkyB.backend.common.exception.ErrorCode.MATCH_NOT_FOUND;

public class MatchNotFoundException extends LinkyBusinessException {

    public MatchNotFoundException() {
        super(MATCH_NOT_FOUND);
    }
}
