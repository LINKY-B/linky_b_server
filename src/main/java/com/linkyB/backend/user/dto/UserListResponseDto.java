package com.linkyB.backend.user.dto;

import com.linkyB.backend.user.domain.User;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class UserListResponseDto {
    private long userId;
    private String userNickName;
    private String userMajorName;
    private String userStudentNum;
    private String userProfileImg;
    private int userLikeCount;
    private List<String> userInterest;

    public UserListResponseDto(User user) {
        this.userId = user.getUserId();
        this.userNickName = user.getUserNickName();
        this.userMajorName = user.getUserMajorName();
        this.userStudentNum = user.getUserStudentNum();
        this.userProfileImg = user.getUserProfileImg();
        this.userLikeCount = user.getUserLikeCount();
        this.userInterest = user.getUserInterest().stream().map(list -> {
            return list.getUserInterest();
        }).collect(Collectors.toList());
    }

}
