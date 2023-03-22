package com.linkyB.backend.user.service;

import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;
import com.linkyB.backend.common.util.SecurityUtils;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.domain.UserNotification;
import com.linkyB.backend.user.domain.UserStatusForMyInfo;
import com.linkyB.backend.user.dto.PatchUserReq;
import com.linkyB.backend.user.dto.UserDetailDto;
import com.linkyB.backend.user.dto.UserDto;
import com.linkyB.backend.user.dto.UserSignupResponseDto;
import com.linkyB.backend.user.exception.UserNotFoundException;
import com.linkyB.backend.user.mapper.UserMapper;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

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
    public UserDetailDto findUser(Long userId) {
        User users = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        UserDetailDto dto = UserMapper.INSTANCE.UserdetaildtoToEntity(users);

        return dto;
    }

    @Transactional
    // 알림 활성화
    public UserDto activeAlarm(long userId) {
        User users = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        users.updateUserNotification(UserNotification.ACTIVE);
        UserDto dto = UserMapper.INSTANCE.entityToDto(users);

        return dto;
    }

    @Transactional
    // 알림 비활성화
    public UserDto inactiveAlarm(long userId) {
        User users = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        users.updateUserNotification(UserNotification.INACTIVE);
        UserDto dto = UserMapper.INSTANCE.entityToDto(users);

        return dto;
    }

    // 정보 활성화
    @Transactional
    public UserDto activeUser(long userId) {
        User users = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        users.updateStatusForMyInfo(UserStatusForMyInfo.ACTIVE);
        UserDto dto = UserMapper.INSTANCE.entityToDto(users);

        return dto;
    }

    // 정보 비활성화
    @Transactional
    public UserDto inactiveUser(long userId) {
        User users = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        users.updateStatusForMyInfo(UserStatusForMyInfo.INACTIVE);
        UserDto dto = UserMapper.INSTANCE.entityToDto(users);

        return dto;
    }

    // 유저 프로필 수정
    @Transactional
    public UserDto modifyProfile(PatchUserReq dto, MultipartFile multipartFile) throws IOException {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if(multipartFile != null){
            String storedFileName = s3Uploader.upload(multipartFile, "images/");
            user.updateProfileImage(storedFileName);
        }
        user.updateInfo(dto);

        UserDto res = UserMapper.INSTANCE.entityToDto(user);

        return res;

    }

    // 유저 탈퇴
    @Transactional
    public UserDto deleteUser(long TokenUser, long userId) {

        User users = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (users.getUserId() == TokenUser) {
            userRepository.delete(users);
            UserDto dto = UserMapper.INSTANCE.entityToDto(users);
            return dto;
        } else
            throw new LinkyBusinessException("탈퇴 권한이 없습니다.", ErrorCode.AUTHORITY_INVALID);
    }
}
