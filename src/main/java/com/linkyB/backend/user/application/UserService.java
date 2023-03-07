package com.linkyB.backend.user.application;

import com.linkyB.backend.block.dto.PatchBlockReq;
import com.linkyB.backend.block.repository.BlockRepository;
import com.linkyB.backend.common.exception.LInkyBussinessException;
import com.linkyB.backend.user.converter.UserConverter;
import com.linkyB.backend.user.domain.*;
import com.linkyB.backend.user.mapper.UserMapper;
import com.linkyB.backend.user.presentation.dto.*;
import com.linkyB.backend.user.repository.UserInterestRepository;
import com.linkyB.backend.user.repository.UserPersonalityRepository;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserInterestRepository interestRepository;
    private final UserPersonalityRepository personalityRepository;
    private final UserConverter userConverter;
    private final S3Uploader s3Uploader;


    public UserSignupResponseDto findUserById(Long userId) {
        return userRepository.findById(userId)
                .map(UserSignupResponseDto::of)
                .orElseThrow(() -> new LInkyBussinessException("해당하는 유저가 없습니다.", HttpStatus.BAD_REQUEST));
    }

    public UserSignupResponseDto findUserByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .map(UserSignupResponseDto::of)
                .orElseThrow(() -> new LInkyBussinessException("해당하는 유저가 없습니다.", HttpStatus.BAD_REQUEST));
    }

    // 유저 정보 상세 조회
    public UserDetailDto findUser(Long userId) {
        User users = userRepository.findById(userId)
                .orElseThrow(() -> new LInkyBussinessException("해당하는 유저가 없습니다.", HttpStatus.BAD_REQUEST));
        UserDetailDto dto = UserMapper.INSTANCE.UserdetaildtoToEntity(users);
        return dto;
    }

    @Transactional
    // 알림 활성화
    public UserDto activeAlaram(long userId) {
        User users = userRepository.findById(userId)
                .orElseThrow(() -> new LInkyBussinessException("해당하는 유저가 없습니다.", HttpStatus.BAD_REQUEST));

        users.updateUserNotification(UserNotification.ACTIVE);
        UserDto dto = UserMapper.INSTANCE.entityToDto(users);

        return dto;
    }

    @Transactional
    // 알림 비활성화
    public UserDto inactiveAlaram(long userId) {
        User users = userRepository.findById(userId)
                .orElseThrow(() -> new LInkyBussinessException("해당하는 유저가 없습니다.", HttpStatus.BAD_REQUEST));

        users.updateUserNotification(UserNotification.INACTIVE);
        UserDto dto = UserMapper.INSTANCE.entityToDto(users);

        return dto;
    }

    // 정보 활성화
    @Transactional
    public UserDto activeUser(long userId) {
        User users = userRepository.findById(userId)
                .orElseThrow(() -> new LInkyBussinessException("해당하는 유저가 없습니다.", HttpStatus.BAD_REQUEST));

        users.updateStatusForMyInfo(UserStatusForMyInfo.ACTIVE);
        UserDto dto = UserMapper.INSTANCE.entityToDto(users);

        return dto;
    }

    // 정보 비활성화
    @Transactional
    public UserDto inactiveUser(long userId) {
        User users = userRepository.findById(userId)
                .orElseThrow(() -> new LInkyBussinessException("해당하는 유저가 없습니다.", HttpStatus.BAD_REQUEST));

        users.updateStatusForMyInfo(UserStatusForMyInfo.INACTIVE);
        UserDto dto = UserMapper.INSTANCE.entityToDto(users);

        return dto;
    }

    // 유저 프로필 수정
    @Transactional
    public UserDto modifyProfile(long userId, PatchUserReq dto, MultipartFile multipartFile)throws IOException {
        User users = userRepository.findById(userId)
                .orElseThrow(() -> new LInkyBussinessException("해당하는 유저가 없습니다.", HttpStatus.BAD_REQUEST));

        String storedFileName = s3Uploader.upload(multipartFile, "images/");
        users.updateInfo(dto, storedFileName);

        Optional<User> findUser = userRepository.findById(userId);
        UserInterestDto interestDto = new UserInterestDto(findUser.get(), findUser.get().getUserInterest());
        UserPersonalityDto personalityDto = new UserPersonalityDto(findUser.get(), findUser.get().getUserPersonality());

        if(!interestRepository.findAllByUser(userId).isEmpty()){
            interestRepository.deleteAllByUserId(userId);
        }
        List<Interest> interestList = dto.getInterestList();
        for(Interest n : interestList)
            interestRepository.save(userConverter.interestSave(users, n));


        if(!personalityRepository.findAllByUser(userId).isEmpty()){
            personalityRepository.deleteAllByUserId(userId);
        }
        List<Personality> personalityList = dto.getPersonalities();
        for(Personality i : personalityList)
            personalityRepository.save(userConverter.personalitySave(users, i));

        UserDto res = UserMapper.INSTANCE.entityToDto(users);

        return res;

    }
}
