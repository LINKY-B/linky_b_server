package com.linkyB.backend.user.presentation.dto;

import com.linkyB.backend.user.domain.Authority;
import com.linkyB.backend.user.domain.Interest;
import com.linkyB.backend.user.domain.Personality;
import com.linkyB.backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequestDto {
    @NotBlank
    private String userEmail;

    @NotBlank
    private String userName;
    @NotBlank
    @Size(min = 2, max = 8)
    private String userNickName;
    @NotBlank
    private String userBirth;
    @NotBlank
    //영문, 숫자, 특수문자 포함
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$")
    @Size(min = 7)
    private String userPassword;
    @NotBlank
    private String userSchoolName;
    @NotBlank
    private String userMajorName;
    @NotBlank
    private String userStudentNum;
    @NotBlank
    private String gradeStatus;

    @NotBlank
    private String userSex;
    @NotBlank
    private String userMBTI;

    private List<Interest> userInterests;
    private List<Personality> userPersonalities;

    private String userSelfIntroduction;

    public User toUser(PasswordEncoder passwordEncoder, List<Interest> userInterests
            , List<Personality> userPersonalities, String profileImg, String schoolImg) {
        return User.builder()
                .userEmail(userEmail)
                .userPassword(passwordEncoder.encode(userPassword))
                .userName(userName)
                .userNickName(userNickName)
                .userBirth(userBirth)
                .userSchoolName(userSchoolName)
                .userMajorName(userMajorName)
                .userSex(userSex)
                .userProfileImg(profileImg)
                .gradStatus(gradeStatus)
                .userSchoolImg(schoolImg)
                .userMBTI(userMBTI)
                .userStudentNum(userStudentNum)
                .userPersonality(userPersonalities)
                .userInterest(userInterests)
                .userSelfIntroduction(userSelfIntroduction)
                .authority(Authority.ROLE_USER)
                .build();
    }

}
