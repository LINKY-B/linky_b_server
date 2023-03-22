package com.linkyB.backend.filter.converter;

import com.linkyB.backend.filter.entity.GenderForFilter;
import com.linkyB.backend.filter.entity.GradeForFilter;
import com.linkyB.backend.filter.entity.MajorForFilter;
import com.linkyB.backend.filter.entity.MbtiForFilter;
import com.linkyB.backend.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class FilterConverter {
    public GenderForFilter filterSave(final User userId, final GenderForFilter dto) {
        return GenderForFilter.builder()
                .user(userId)
                .gender(dto.getGender())
                .build();
    }

    public MbtiForFilter mbtiFiltersave(final User user, final MbtiForFilter dto) {
        return MbtiForFilter.builder()
                .user(user)
                .blockedMbti(dto.getBlockedMbti())
                .build();
    }


    public GradeForFilter gradeFiltersave(final User user, final GradeForFilter dto) {
        return GradeForFilter.builder()
                .user(user)
                .grade(dto.getGrade())
                .build();
    }

    public MajorForFilter majorFiltersave(final User user, final MajorForFilter dto) {
        return MajorForFilter.builder()
                .user(user)
                .blockedMajor(dto.getBlockedMajor())
                .build();
    }

}
