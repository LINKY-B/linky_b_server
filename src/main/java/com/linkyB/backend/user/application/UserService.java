package com.linkyB.backend.user.application;

import com.linkyB.backend.common.exception.LInkyBussinessException;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.domain.UserNotification;
import com.linkyB.backend.user.mapper.UserMapper;
import com.linkyB.backend.user.presentation.dto.UserDetailDto;
import com.linkyB.backend.user.presentation.dto.UserDto;
import com.linkyB.backend.user.presentation.dto.UserSignupResponseDto;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    // 유저 정보 상세 조회
    public UserDetailDto findUser(Long userId) {
        User users = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
        UserDetailDto dto = UserMapper.INSTANCE.UserdetaildtoToEntity(users);
        return dto;
    }

    @Transactional
    // 알림 활성화
    public UserDto activeAlaram(long userId) {
        User users = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        users.updateUserNotification(UserNotification.ACTIVE);
        UserDto dto = UserMapper.INSTANCE.entityToDto(users);

        return dto;
    }

    @Transactional
    // 알림 비활성화
    public UserDto inactiveAlaram(long userId) {
        User users = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        users.updateUserNotification(UserNotification.INACTIVE);
        UserDto dto = UserMapper.INSTANCE.entityToDto(users);

        return dto;
    }
}
