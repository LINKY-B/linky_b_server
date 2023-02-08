package com.linkyB.backend.user.converter;

import com.linkyB.backend.user.domain.Interest;
import com.linkyB.backend.user.domain.Personality;
import com.linkyB.backend.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public Interest interestSave(final User userId, final Interest dto) {
        return Interest.builder()
                .user(userId)
                .userInterest(dto.getUserInterest())
                .build();
    }

    public Personality personalitySave(final User userId, final Personality dto) {
        return Personality.builder()
                .user(userId)
                .userPersonality(dto.getUserPersonality())
                .build();
    }

}
