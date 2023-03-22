package com.linkyB.backend.common.util;

import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityUtils {

    public static Long getCurrentUserId() {
        try {
            final String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return Long.valueOf(username);
        } catch (Exception e) {
            throw new LinkyBusinessException(ErrorCode.GET_USER_FAILED);
        }
    }
}
