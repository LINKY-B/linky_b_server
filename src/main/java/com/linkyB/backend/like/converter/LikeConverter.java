package com.linkyB.backend.like.converter;

import com.linkyB.backend.like.entity.LikeStatus;
import com.linkyB.backend.like.entity.UserLikes;
import com.linkyB.backend.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class LikeConverter {
    public UserLikes userLikes(final User give, final User get) {
        return UserLikes.builder()
                .userGiveLikes(give)
                .userGetLikes(get)
                .status(LikeStatus.ACTIVE)
                .build();
    }
}
