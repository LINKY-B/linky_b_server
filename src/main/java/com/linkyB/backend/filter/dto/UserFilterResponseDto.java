package com.linkyB.backend.filter.dto;

import com.linkyB.backend.user.domain.User;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder

public class UserFilterResponseDto {
    private List<String> userGenderForFilters;
    private List<String> userGradeForFilters;
    private List<String> userMajorForFilters;
    private List<String> userMbtiForFilters;

    public static UserFilterResponseDto of(User user) {
        UserFilterResponseDto dto = new UserFilterResponseDto();
        return UserFilterResponseDto.builder()
                .userGenderForFilters(user.getUserGenderForFilters().stream().map(list -> {
                        return list.getGender();
            }).collect(Collectors.toList()))
                .userGradeForFilters(user.getUserGradeForFilters().stream().map(list -> {
                    return list.getGrade();
                }).collect(Collectors.toList()))
                .userMajorForFilters(user.getUserMajorForFilters().stream().map(list -> {
                    return list.getBlockedMajor();
                }).collect(Collectors.toList()))
                .userMbtiForFilters(user.getUserMbtiForFilters().stream().map(list -> {
                    return list.getBlockedMbti();
                }).collect(Collectors.toList()))
                .build();
    }

}
