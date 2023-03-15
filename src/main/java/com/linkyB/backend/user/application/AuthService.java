package com.linkyB.backend.user.application;

import com.linkyB.backend.common.exception.LInkyBussinessException;
import com.linkyB.backend.user.domain.Interest;
import com.linkyB.backend.user.domain.Personality;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.presentation.dto.*;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    private final UserRepository userRepository;
    private final UserInterestRepository interestRepository;
    private final UserPersonalityRepository personalityRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    private final String SERVER = "Server";

    @Transactional
    public UserSignupResponseDto signup(UserSignupRequestDto userSignupRequestDto, MultipartFile profileImg, MultipartFile schoolImg) throws IOException {
        if (userRepository.existsByUserEmail(userSignupRequestDto.getUserEmail())) {
            throw new LInkyBussinessException("이미 가입되어 있는 유저입니다.", HttpStatus.BAD_REQUEST);
        }

        //유저 성격, 관시사 정보를 리스트로 반환
        List<Interest> userInterests = userSignupRequestDto.getUserInterests();
        List<Personality> userPersonalities = userSignupRequestDto.getUserPersonalities();

        String userProfileImg = s3Uploader.upload(profileImg,"images/profileImg/");
        String userSchoolImg = s3Uploader.upload(schoolImg,"images/school/");

        User user = userSignupRequestDto.toUser(passwordEncoder, userInterests, userPersonalities,userProfileImg,userSchoolImg);
        userRepository.save(user);

        //저장한 유저의 id를 관심사와 성격을 외래키로 저장
        Optional<User> findUser = userRepository.findByUserEmail(user.getUserEmail());
        UserInterestDto interestDto = new UserInterestDto(findUser.get(), findUser.get().getUserInterest());
        UserPersonalityDto personalityDto = new UserPersonalityDto(findUser.get(), findUser.get().getUserPersonality());
        interestRepository.saveAll(interestDto.toInterest());
        personalityRepository.saveAll(personalityDto.toPersonality());

        return UserSignupResponseDto.of(user);
    }

    @Transactional
    public TokenDto login(UserLoginDto loginDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User dto = userRepository.findByUserEmail(loginDto.getEmail())
                .orElseThrow(() -> new LInkyBussinessException("해당하는 유저가 없습니다.", HttpStatus.BAD_REQUEST));
        return generateToken(SERVER, authentication.getName(), dto.getUserId(), getAuthorities(authentication));
    }

    // AT가 만료일자만 초과한 유효한 토큰인지 검사
    public boolean validate(String requestAccessTokenInHeader) {
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);
        return jwtTokenProvider.validateAccessTokenOnlyExpired(requestAccessToken); // true = 재발급
    }

    // 토큰 재발급: validate 메서드가 true 반환할 때만 사용 -> AT, RT 재발급
    @Transactional
    public TokenDto reissue(String requestAccessTokenInHeader, String requestRefreshToken) {
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);

        Authentication authentication = jwtTokenProvider.getAuthentication(requestAccessToken);
        String principal = getPrincipal(requestAccessToken);

        String refreshTokenInRedis = redisService.getValues("RT(" + SERVER + "):" + principal);
        if (refreshTokenInRedis == null) { // Redis에 저장되어 있는 RT가 없을 경우
            return null; // -> 재로그인 요청
        }

        // 요청된 RT의 유효성 검사 & Redis에 저장되어 있는 RT와 같은지 비교
        if(!jwtTokenProvider.validateRefreshToken(requestRefreshToken) || !refreshTokenInRedis.equals(requestRefreshToken)) {
            redisService.deleteValues("RT(" + SERVER + "):" + principal); // 탈취 가능성 -> 삭제
            return null; // -> 재로그인 요청
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authorities = getAuthorities(authentication);

        long users = jwtTokenProvider.getClaims(requestAccessToken).size();
        User user = userRepository.findById(users)
                .orElseThrow(() -> new LInkyBussinessException("해당하는 유저가 없습니다.", HttpStatus.BAD_REQUEST));

        // 토큰 재발급 및 Redis 업데이트
        redisService.deleteValues("RT(" + SERVER + "):" + principal); // 기존 RT 삭제
        TokenDto tokenDto = jwtTokenProvider.createToken(principal, user.getUserId(), authorities);
        saveRefreshToken(SERVER, principal, tokenDto.getRefreshToken());
        return tokenDto;
    }

    // 토큰 발급
    @Transactional
    public TokenDto generateToken(String provider, String email, long user, String authorities) {
        // RT가 이미 있을 경우
        if(redisService.getValues("RT(" + provider + "):" + user) != null) {
            redisService.deleteValues("RT(" + provider + "):" + user); // 삭제
        }

        // AT, RT 생성 및 Redis에 RT 저장
        TokenDto tokenDto = jwtTokenProvider.createToken(email, user, authorities);
        saveRefreshToken(provider, email, tokenDto.getRefreshToken());
        return tokenDto;
    }

    // RT를 Redis에 저장
    @Transactional
    public void saveRefreshToken(String provider, String principal, String refreshToken) {
        redisService.setValuesWithTimeout("RT(" + provider + "):" + principal, // key
                refreshToken, // value
                jwtTokenProvider.getTokenExpirationTime(refreshToken)); // timeout(milliseconds)
    }

    // 권한 이름 가져오기
    public String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    // AT로부터 principal 추출
    public String getPrincipal(String requestAccessToken) {
        return jwtTokenProvider.getAuthentication(requestAccessToken).getName();
    }

    // "Bearer {AT}"에서 {AT} 추출
    public String resolveToken(String requestAccessTokenInHeader) {
        if (requestAccessTokenInHeader != null && requestAccessTokenInHeader.startsWith("Bearer ")) {
            return requestAccessTokenInHeader.substring(7);
        }
        return null;
    }

    // 로그아웃
    @Transactional
    public void logout(String requestAccessTokenInHeader) {
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);
        String principal = getPrincipal(requestAccessToken);

        // Redis에 저장되어 있는 RT 삭제
        String refreshTokenInRedis = redisService.getValues("RT(" + SERVER + "):" + principal);
        if (refreshTokenInRedis != null) {
            redisService.deleteValues("RT(" + SERVER + "):" + principal);
        }

        // Redis에 로그아웃 처리한 AT 저장
        long expiration = jwtTokenProvider.getTokenExpirationTime(requestAccessToken) - new Date().getTime();
        redisService.setValuesWithTimeout(requestAccessToken,
                "logout",
                expiration);
    }

    @Transactional
    public UserPasswordDto updatePassword(UserPasswordDto passwordRequestDto) {
        // db에서 해당 핸드폰으로 저장된 유저정보 가져오기
        if (!userRepository.existsByUserEmail(passwordRequestDto.getEmail())) {
            throw new LInkyBussinessException("회원가입된 유저가 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        User findPhone = userRepository.findByUserEmail(passwordRequestDto.getEmail()).get();

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
