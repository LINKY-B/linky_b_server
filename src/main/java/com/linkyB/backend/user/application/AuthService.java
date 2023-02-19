package com.linkyB.backend.user.application;

import com.linkyB.backend.common.exception.LInkyBussinessException;
import com.linkyB.backend.user.domain.Interest;
import com.linkyB.backend.user.domain.Personality;
import com.linkyB.backend.user.domain.RefreshToken;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.presentation.dto.*;
import com.linkyB.backend.user.repository.RefreshTokenRepository;
import com.linkyB.backend.user.repository.UserInterestRepository;
import com.linkyB.backend.user.repository.UserPersonalityRepository;
import com.linkyB.backend.user.repository.UserRepository;
import com.linkyB.backend.user.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final UserInterestRepository interestRepository;
    private final UserPersonalityRepository personalityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public UserSignupResponseDto signup(UserSignupRequestDto userSignupRequestDto, MultipartFile multipartFile) throws IOException {
        if (userRepository.existsByUserPhone(userSignupRequestDto.getUserPhone())) {
            throw new LInkyBussinessException("이미 가입되어 있는 유저입니다.", HttpStatus.BAD_REQUEST);
        }

        //유저 성격, 관시사 정보를 리스트로 반환
        List<Interest> userInterests = userSignupRequestDto.getUserInterests();
        List<Personality> userPersonalities = userSignupRequestDto.getUserPersonalities();

        String storedFileName = s3Uploader.upload(multipartFile,"images/");

        User user = userSignupRequestDto.toUser(passwordEncoder, userInterests, userPersonalities,storedFileName);
        userRepository.save(user);

        //저장한 유저의 id를 관심사와 성격을 외래키로 저장
        Optional<User> findUser = userRepository.findByUserPhone(user.getUserPhone());
        UserInterestDto interestDto = new UserInterestDto(findUser.get(), findUser.get().getUserInterest());
        UserPersonalityDto personalityDto = new UserPersonalityDto(findUser.get(), findUser.get().getUserPersonality());
        interestRepository.saveAll(interestDto.toInterest());
        personalityRepository.saveAll(personalityDto.toPersonality());

        return UserSignupResponseDto.of(user);
    }

    @Transactional
    public TokenDto login(UserLoginDto userLoginDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = userLoginDto
                .toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new LInkyBussinessException("Refresh Token이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 User ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(authentication.getName())
                .orElseThrow(() -> new LInkyBussinessException("로그아웃된 사용자입니다.", HttpStatus.BAD_REQUEST));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new LInkyBussinessException("토큰의 유저 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

    @Transactional
    public UserPasswordDto updatePassword(UserPasswordDto passwordRequestDto) {
        // db에서 해당 핸드폰으로 저장된 유저정보 가져오기
        if (!userRepository.existsByUserPhone(passwordRequestDto.getPhone())) {
            throw new LInkyBussinessException("회원가입된 유저가 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        User findPhone = userRepository.findByUserPhone(passwordRequestDto.getPhone()).get();

        //기존 비밀번호와 같은지 확인하기
        if (passwordEncoder.matches(passwordRequestDto.getPassword(), findPhone.getUserPassword())) {
            throw new LInkyBussinessException("이전 비밀번호와 동일합니다.", HttpStatus.BAD_REQUEST);
        }

        //비밀번호 변경후 db에 저장
        findPhone.updatePassword(passwordEncoder.encode(passwordRequestDto.getPassword()));
        userRepository.save(findPhone);

        return passwordRequestDto;
    }
}
