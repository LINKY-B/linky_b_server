package com.linkyB.backend.user.dto;

import com.linkyB.backend.user.domain.User;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class UserDetailResponseDto {
    private Long userId;
    private String userNickName;
    private String userMajorName;
    private String userStudentNum;
    private String userBirth;
    private String userSex;
    private String userSelfIntroduction;
    private String userProfileImg;
    private String userMBTI;
    private List<String> userInterest;
    private List<String> userPersonality;
    private int userLikeCount;
    private int userMatchingCount;

    public static UserDetailResponseDto of(User user) {
        return UserDetailResponseDto.builder()
                .userId(user.getUserId())
                .userNickName(user.getUserNickName())
                .userSelfIntroduction(user.getUserSelfIntroduction())
                .userMajorName(user.getUserMajorName())
                .userStudentNum(user.getUserStudentNum())
                .userBirth(user.getUserBirth())
                .userSex(user.getUserSex())
                .userProfileImg(user.getUserProfileImg())
                .userMBTI(user.getUserMBTI())
                .userLikeCount(user.getUserLikeCount())
                .userMatchingCount(user.getUserMatchingCount())
                .userInterest(user.getUserInterest().stream().map(list -> {
                    return list.getUserInterest();
                }).collect(Collectors.toList()))
                .userPersonality(user.getUserPersonality().stream().map(list -> {
                    return list.getUserPersonality();
                }).collect(Collectors.toList()))
                .build();
        }
    }


