package com.linkyB.backend.user.dto;

import com.linkyB.backend.user.domain.Interest;
import com.linkyB.backend.user.domain.Personality;
import com.linkyB.backend.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Schema(description = "회원 가입 요청 DTO")
public class UserSignupRequestDto {
    @NotBlank(message = "이메일은 필수입력입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String userEmail;

    @NotBlank(message = "아이디는 필수입력입니다.")
    private String userName;

    @NotBlank(message = "이메일 인증 코드는 필수입니다.")
    private String authCode;

    @NotBlank(message = "닉네임은 필수입력입니다.")
    @Size(min = 2, max = 8, message = "닉네임의 길이는 2이상 8이하여야합니다.")
    private String userNickName;
    @NotBlank(message = "생일은 필수입력입니다.")
    private String userBirth;
    @NotBlank(message = "비밀번호는 필수입력입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$", message = "비밀번호는 영문, 숫자, 특수문자를 하나씩 포함해야합니다.")
    @Size(min = 7, message = "비밀번호는 길이가 7이상이어야합니다.")
    private String userPassword;
    @NotBlank(message = "학교 이름은 필수입력입니다.")
    private String userSchoolName;
    @NotBlank(message = "전공이름은 필수입력입니다.")
    private String userMajorName;
    @NotBlank(message = "입학년도는 필수입력입니다.")
    private String userStudentNum;

    private String gradeStatus;

    @NotBlank(message = "성별은 필수입력입니다.")
    private String userSex;
    @NotBlank(message = "MBTI는 필수입력입니다.")
    private String userMBTI;

    private List<String> userInterests = new ArrayList<>();
    private List<String> userPersonalities = new ArrayList<>();

    private String userSelfIntroduction;

    public User toEntity(PasswordEncoder passwordEncoder, String profileImg, String schoolImg) {

        final List<Interest> interests = userInterests.stream().map(Interest::new).collect(Collectors.toList());
        final List<Personality> personalities = userPersonalities.stream().map(Personality::new).collect(Collectors.toList());

        User user = User.builder()
                .userEmail(userEmail)
                .userPassword(passwordEncoder.encode(userPassword))
                .userName(userName)
                .userNickName(userNickName)
                .userBirth(userBirth)
                .userSchoolName(userSchoolName)
                .userMajorName(userMajorName)
                .userSex(userSex)
                .userProfileImg(profileImg)
                .userSchoolImg(schoolImg)
                .gradStatus(gradeStatus)
                .userMBTI(userMBTI)
                .userStudentNum(userStudentNum)
                .userSelfIntroduction(userSelfIntroduction)
                .build();

        interests.forEach((i) -> user.addInterests(i));
        personalities.forEach((p) -> user.addPersonality(p));

        return user;
    }

}
