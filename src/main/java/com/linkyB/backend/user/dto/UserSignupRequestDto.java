package com.linkyB.backend.user.dto;

import com.linkyB.backend.user.domain.Interest;
import com.linkyB.backend.user.domain.Personality;
import com.linkyB.backend.user.domain.User;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
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
    @ApiModelProperty(value = "이메일", example = "real@naver.com", required = true)
    @NotBlank(message = "이메일은 필수입력입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String userEmail;

    @ApiModelProperty(value = "사용자 이름", example = "이지은", required = true)
    @NotBlank(message = "사용자 이름은 필수입력입니다.")
    private String userName;

    @ApiModelProperty(value = "이메일 인증코드", example = "3F23A6", required = true)
    @NotBlank(message = "이메일 인증 코드는 필수입니다.")
    private String authCode;

    @ApiModelProperty(value = "닉네임", example = "아이유", required = true)
    @NotBlank(message = "닉네임은 필수입력입니다.")
    @Size(min = 2, max = 8, message = "닉네임의 길이는 2이상 8이하여야합니다.")
    private String userNickName;

    @ApiModelProperty(value = "비밀번호", example = "qlalfqjsgh1!", required = true)
    @NotBlank(message = "비밀번호는 필수입력입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$", message = "비밀번호는 영문, 숫자, 특수문자를 하나씩 포함해야합니다.")
    @Size(min = 7, message = "비밀번호는 길이가 7이상이어야합니다.")
    private String userPassword;

    @ApiModelProperty(value = "학교 이름", example = "숭실대학교", required = true)
    @NotBlank(message = "학교 이름은 필수입력입니다.")
    private String userSchoolName;

    @ApiModelProperty(value = "전공 이름", example = "소프트웨어학부", required = true)
    @NotBlank(message = "전공이름은 필수입력입니다.")
    private String userMajorName;

    @ApiModelProperty(value = "입학년도", example = "20", required = true)
    @NotBlank(message = "입학년도는 필수입력입니다.")
    private String userStudentNum;

    @ApiModelProperty(value = "생일", example = "2000-00-00", required = true)
    @NotBlank(message = "생일은 필수입력입니다.")
    private String userBirth;

    @ApiModelProperty(value = "졸업 여부", example = "false", required = false)
    private String gradeStatus = "false";

    @ApiModelProperty(value = "성별", example = "남", required = false)
    private String userSex = "";

    @ApiModelProperty(value = "MBTI", example = "ISTP", required = false)
    private String userMBTI = "";

    @ApiModelProperty(value = "프로필 이미지", example = "https://linkyb-bucket.s3.ap-northeast-2.amazonaws.com/images/profileImg/M11.svg", required = false)
    @URL(message = "URL 형식이 아닙니다.")
    private String profileImg = "https://linkyb-bucket.s3.ap-northeast-2.amazonaws.com/images/profileImg/M11.svg";

    @ApiModelProperty(value = "흥미", example = "[\"탁구치기\", \"게임하기\"]", required = false)
    private List<String> userInterests = new ArrayList<>();

    @ApiModelProperty(value = "성격", example = "[\"조용함\", \"차분함\"]", required = false)
    private List<String> userPersonalities = new ArrayList<>();

    @ApiModelProperty(value = "자기소개글", example = "안녕하세요! 제 게시글입니다!", required = false)
    private String userSelfIntroduction = "";

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
