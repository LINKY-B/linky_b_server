package com.linkyB.backend.user.application;

import com.linkyB.backend.config.BaseResponse;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserRepository userRepository;

    public long getUserId() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByMail(userEmail);
        long Idx = user.getUserId();
        return Idx;
    }
}
