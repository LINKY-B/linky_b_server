package com.linkyB.backend.user.presentation.dto;

import com.linkyB.backend.user.domain.Interest;
import com.linkyB.backend.user.domain.Personality;
import com.linkyB.backend.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserPersonalityDto {
    private User user;
    private List<Personality> personalities;

    public UserPersonalityDto(User user, List<Personality> personality) {
        this.user = user;
        this.personalities = personality;
    }

    //Personality entity를 리스트 형식으로 저장
    public List<Personality> toPersonality() {
        List<Personality> userPersonality = new ArrayList<>();

        for (Personality personality : personalities) {
            Personality getPersonality = Personality.builder()
                    .userPersonality(personality.getUserPersonality())
                    .user(user)
                    .build();

            userPersonality.add(getPersonality);
        }

        return userPersonality;
    }
}
