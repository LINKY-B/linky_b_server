package com.linkyB.backend.user.presentation.dto;

import com.linkyB.backend.user.domain.Interest;
import lombok.*;

import java.util.List;
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class UserListDto {
    private String userNickName;
    private String userMajorName;
    private String userStudentNum;
    private String userProfileImg;
    private int userLikeCount;

    private List<Interest> userInterest;
}
