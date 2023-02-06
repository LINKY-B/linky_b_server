package com.linkyB.backend.user.application;

import com.linkyB.backend.user.presentation.dto.UserSignupResponseDto;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserSignupResponseDto findUserById(Long userId) {
        return userRepository.findById(userId)
                .map(UserSignupResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    public UserSignupResponseDto findUserByPhone(String phone) {
        return userRepository.findByUserPhone(phone)
                .map(UserSignupResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }
}
