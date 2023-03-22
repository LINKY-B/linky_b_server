package com.linkyB.backend.user.presentation.dto;

import com.linkyB.backend.user.domain.Interest;
import com.linkyB.backend.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserInterestDto {
    private User user;
    private List<Interest> interests;

    public UserInterestDto(User user, List<Interest> interests) {
        this.user = user;
        this.interests = interests;
    }

    public List<Interest> toInterest() {
        List<Interest> userInterest = new ArrayList<>();

        for (Interest interest : interests) {
            Interest interest1 = Interest.builder()
                    .userInterest(interest.getUserInterest())
                    .user(user)
                    .build();

            userInterest.add(interest1);
        }

        return userInterest;
    }
}
