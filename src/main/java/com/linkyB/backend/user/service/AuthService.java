package com.linkyB.backend.user.service;

import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.domain.redis.EmailCode;
import com.linkyB.backend.user.dto.UserPasswordDto;
import com.linkyB.backend.user.dto.UserSignupRequestDto;
import com.linkyB.backend.user.dto.UserSignupResponseDto;
import com.linkyB.backend.user.exception.UserNotFoundException;
import com.linkyB.backend.user.repository.UserRepository;
import com.linkyB.backend.user.repository.redis.EmailCodeRepository;
import com.linkyB.backend.user.util.UserProfileUrls;
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
    private final EmailCodeRepository emailCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    @Transactional
    public UserSignupResponseDto signup(UserSignupRequestDto signupRequestDto, MultipartFile schoolImg) throws IOException {
        if (userRepository.existsByUserEmail(signupRequestDto.getUserEmail())) {
            throw new LinkyBusinessException(ErrorCode.USERNAME_ALREADY_EXIST);
        }

        // 존재하는 프로필 이미지인지 확인
        final String userProfileImg = signupRequestDto.getProfileImg();
        if(!UserProfileUrls.PROFILE_IMAGE_URLS.contains(userProfileImg)){
            throw new LinkyBusinessException(ErrorCode.PROFILE_IMAGE_NOT_FOUND);
        }

        // 이메일 인증 코드 확인
        EmailCode emailCode = emailCodeRepository
                .findByEmailAndUserName(signupRequestDto.getUserEmail(), signupRequestDto.getUserName())
                .orElseThrow(() -> new LinkyBusinessException(ErrorCode.EMAIL_NOT_CONFIRMED));

        if (!emailCode.getCode().equals(signupRequestDto.getAuthCode())) {
            throw new LinkyBusinessException(ErrorCode.CONFIRM_CODE_NOT_VALID);
        }

        final String userSchoolImg = s3Uploader.upload(schoolImg, "images/school/");

        User user = signupRequestDto.toEntity(passwordEncoder, userProfileImg, userSchoolImg);
        userRepository.save(user);

        return UserSignupResponseDto.of(user);
    }


    // 로그아웃
    @Transactional
    public UserPasswordDto updatePassword(UserPasswordDto passwordRequestDto) {
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
