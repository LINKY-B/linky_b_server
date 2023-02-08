package com.linkyB.backend.user.presentation.dto;

import com.linkyB.backend.user.domain.Interest;
import com.linkyB.backend.user.domain.Personality;
import com.linkyB.backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatchUserReq {
    private String userMajorName;
    private String userMbti;
    private String userSelfIntroduction;
    private List<Interest> interestList;
    private List<Personality> personalities;

}
