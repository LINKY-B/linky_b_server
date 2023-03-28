package com.linkyB.backend.like.dto;

import com.linkyB.backend.like.entity.UserLikes;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class LikeDto {
    private long likeId;

    public static LikeDto of(UserLikes likes) {
        return LikeDto.builder()
                .likeId(likes.getLikeId())
                .build();
    }
}
