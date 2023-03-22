package com.linkyB.backend.user.dto;

import com.linkyB.backend.user.domain.Interest;
import com.linkyB.backend.user.domain.Personality;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class UserDetailDto {
    private Long userId;
    private String userNickName;
    private String userMajorName;
    private String userStudentNum;
    private String userBirth;
    private String userSex;
    private String userSelfIntroduction;
    private String userProfileImg;
    private String userMBTI;
    private List<Interest> userInterest;
    private List<Personality> userPersonality;
    private int userLikeCount;
    private int userMatchingCount;

}
