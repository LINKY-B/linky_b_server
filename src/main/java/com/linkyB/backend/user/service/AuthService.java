package com.linkyB.backend.user.service;

import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.dto.UserPasswordDto;
import com.linkyB.backend.user.dto.UserSignupRequestDto;
import com.linkyB.backend.user.dto.UserSignupResponseDto;
import com.linkyB.backend.user.exception.UserNotFoundException;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;


    @Transactional
    public UserSignupResponseDto signup(UserSignupRequestDto userSignupRequestDto, MultipartFile profileImg, MultipartFile schoolImg) throws IOException {
        if (userRepository.existsByUserEmail(userSignupRequestDto.getUserEmail())) {
            throw new LinkyBusinessException(ErrorCode.USERNAME_ALREADY_EXIST);
        }

        String userProfileImg = s3Uploader.upload(profileImg,"images/profileImg/");
        String userSchoolImg = s3Uploader.upload(schoolImg,"images/school/");

        User user = userSignupRequestDto.toEntity(passwordEncoder, userProfileImg,userSchoolImg);
        userRepository.save(user);

        return UserSignupResponseDto.of(user);
    }


    // 로그아웃


    @Transactional
    public UserPasswordDto updatePassword(UserPasswordDto passwordRequestDto) {
        // db에서 해당 핸드폰으로 저장된 유저정보 가져오기
        if (!userRepository.existsByUserEmail(passwordRequestDto.getEmail())) {
            throw new UserNotFoundException();
        }

        User findPhone = userRepository.findByUserEmail(passwordRequestDto.getEmail()).get();

        //기존 비밀번호와 같은지 확인하기
        if (passwordEncoder.matches(passwordRequestDto.getPassword(), findPhone.getUserPassword())) {
            throw new LinkyBusinessException(ErrorCode.UPDATE_PASSWORD_FAILED);
        }

        //비밀번호 변경후 db에 저장
        findPhone.updatePassword(passwordEncoder.encode(passwordRequestDto.getPassword()));
        userRepository.save(findPhone);

        return passwordRequestDto;
    }


}
