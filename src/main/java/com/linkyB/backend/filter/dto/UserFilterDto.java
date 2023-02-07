package com.linkyB.backend.filter.dto;


import com.linkyB.backend.filter.entity.GenderForFilter;
import com.linkyB.backend.filter.entity.GradeForFilter;
import com.linkyB.backend.filter.entity.MajorForFilter;
import com.linkyB.backend.filter.entity.MbtiForFilter;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class UserFilterDto {

    private List<GenderForFilter> userGenderForFilters;
    private List<GradeForFilter> userGradeForFilters;
    private List<MajorForFilter> userMajorForFilters;
    private List<MbtiForFilter> userMbtiForFilters;

}
