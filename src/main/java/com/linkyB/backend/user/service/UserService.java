package com.linkyB.backend.user.service;

import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;
import com.linkyB.backend.common.util.SecurityUtils;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.domain.enums.UserNotification;
import com.linkyB.backend.user.domain.enums.UserStatusForMyInfo;
import com.linkyB.backend.user.dto.PatchUserReq;
import com.linkyB.backend.user.dto.UserDetailResponseDto;
import com.linkyB.backend.user.dto.UserResponseDto;
import com.linkyB.backend.user.dto.UserSignupResponseDto;
import com.linkyB.backend.user.exception.UserNotFoundException;
import com.linkyB.backend.user.repository.UserRepository;
import com.linkyB.backend.user.util.UserProfileUrls;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

import static com.linkyB.backend.common.exception.ErrorCode.PROFILE_IMAGE_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;


    public UserSignupResponseDto findUserById(Long userId) {
        return userRepository.findById(userId)
                .map(UserSignupResponseDto::of)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserSignupResponseDto findUserByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .map(UserSignupResponseDto::of)
                .orElseThrow(UserNotFoundException::new);
    }

    // 유저 정보 상세 조회
    public UserDetailResponseDto findUser(Long userId) {
        User users = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return UserDetailResponseDto.of(users);

    }

    @Transactional
    // 알림 활성화
    public UserResponseDto activeAlarm(long userId) {
        User users = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        users.updateUserNotification(UserNotification.ACTIVE);

        return UserResponseDto.of(users);
    }

    @Transactional
    // 알림 비활성화
    public UserResponseDto inactiveAlarm(long userId) {
        User users = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        users.updateUserNotification(UserNotification.INACTIVE);

        return UserResponseDto.of(users);
    }

    // 정보 활성화
    @Transactional
    public UserResponseDto activeUser(long userId) {
        User users = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        users.updateStatusForMyInfo(UserStatusForMyInfo.ACTIVE);

        return UserResponseDto.of(users);
    }

    // 정보 비활성화
    @Transactional
    public UserResponseDto inactiveUser(long userId) {
        User users = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        users.updateStatusForMyInfo(UserStatusForMyInfo.INACTIVE);

        return UserResponseDto.of(users);
    }

    // 유저 프로필 수정
    @Transactional
    public UserResponseDto modifyProfile(PatchUserReq dto) {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!UserProfileUrls.isValidProfileImageUrl(dto.getProfileImg())) {
            throw new LinkyBusinessException(PROFILE_IMAGE_NOT_FOUND);
        }

        user.updateInfo(dto);

        return UserResponseDto.of(user);

    }

    // 유저 탈퇴
    @Transactional
    public UserResponseDto deleteUser(long TokenUser, long userId) {
        User users = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (users.getUserId() == TokenUser) {
            userRepository.delete(users);
            return UserResponseDto.of(users);
        } else
            throw new LinkyBusinessException("탈퇴 권한이 없습니다.", ErrorCode.AUTHORITY_INVALID);
    }
}
