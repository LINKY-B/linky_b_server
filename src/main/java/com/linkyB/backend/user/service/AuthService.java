package com.linkyB.backend.user.service;

import com.linkyB.backend.common.exception.LinkyBusinessException;
import com.linkyB.backend.common.result.ResultCode;
import com.linkyB.backend.common.result.ResultResponse;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.domain.redis.EmailCode;
import com.linkyB.backend.user.dto.EmailConfirmCodeRequestDto;
import com.linkyB.backend.user.dto.ResetPasswordRequestDto;
import com.linkyB.backend.user.dto.UserSignupRequestDto;
import com.linkyB.backend.user.dto.UserSignupResponseDto;
import com.linkyB.backend.user.exception.UserNotFoundException;
import com.linkyB.backend.user.repository.UserRepository;
import com.linkyB.backend.user.service.redis.EmailCodeService;
import com.linkyB.backend.user.util.UserProfileUrls;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.linkyB.backend.common.exception.ErrorCode.*;
import static com.linkyB.backend.common.exception.ErrorCode.EMAIL_ALREADY_EXIST;
import static com.linkyB.backend.common.exception.ErrorCode.NICKNAME_ALREADY_EXIST;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final EmailCodeService emailCodeService;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    public boolean isUniqueNickName(String nickName) {
        return !userRepository.existsByUserNickName(nickName);
    }

    @Transactional
    public UserSignupResponseDto signup(UserSignupRequestDto signupRequestDto, MultipartFile schoolImg) throws IOException {
        final String email = signupRequestDto.getUserEmail();
        final String authCode = signupRequestDto.getAuthCode();
        final String nickName = signupRequestDto.getUserNickName();
        final String profileImg = signupRequestDto.getProfileImg();

        if (userRepository.existsByUserEmail(email)) {
            throw new LinkyBusinessException(EMAIL_ALREADY_EXIST);
        }

        if (!isUniqueNickName(nickName)) {
            throw new LinkyBusinessException(NICKNAME_ALREADY_EXIST);
        }

        if (!UserProfileUrls.isValidProfileImageUrl(profileImg)) {
            throw new LinkyBusinessException(PROFILE_IMAGE_NOT_FOUND);
        }

        if (!isValidEmailToken(email, nickName, authCode)) {
            throw new LinkyBusinessException(CONFIRM_CODE_NOT_VALID);
        }

        final String userSchoolImg = s3Uploader.upload(schoolImg, "images/school/");

        User user = signupRequestDto.toEntity(passwordEncoder, profileImg, userSchoolImg);
        userRepository.save(user);

        return UserSignupResponseDto.of(user);
    }


    @Transactional
    public void updatePassword(ResetPasswordRequestDto passwordRequestDto) {
        final String email = passwordRequestDto.getEmail();
        final String authCode = passwordRequestDto.getAuthCode();
        final String newPassword = passwordRequestDto.getPassword();

        User user = userRepository.findByUserEmail(email).orElseThrow(UserNotFoundException::new);
        final String currentPassword = user.getUserPassword();
        final String nickName = user.getUserNickName();

        if (!isValidEmailToken(email, nickName, authCode)) {
            throw new LinkyBusinessException(CONFIRM_CODE_NOT_VALID);
        }

        if (passwordEncoder.matches(newPassword, currentPassword)) {
            throw new LinkyBusinessException(UPDATE_PASSWORD_FAILED);
        }

        user.updatePassword(passwordEncoder.encode(newPassword));
        emailCodeService.removeCode(email, nickName);
    }


    public ResultResponse isValidEmailToken(EmailConfirmCodeRequestDto emailConfirmDto) {
        final String email = emailConfirmDto.getEmail();
        final String nickName = emailConfirmDto.getUserNickName();
        final String authCode = emailConfirmDto.getAuthCode();

        if(!isValidEmailToken(email, nickName, authCode)){
            throw new LinkyBusinessException(CONFIRM_CODE_NOT_VALID);
        }

        return ResultResponse.of(ResultCode.CONFIRM_EMAIL_SUCCESS);
    }


    private boolean isValidEmailToken(String email, String nickName, String authCode) {
        final EmailCode emailCode = emailCodeService.getEmailCode(email, nickName);

        if (emailCode.getCode().equals(authCode)) {
            return true;
        }

        return false;
    }

}
