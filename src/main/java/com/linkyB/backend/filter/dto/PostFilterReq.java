package com.linkyB.backend.filter.dto;

import com.linkyB.backend.filter.entity.GenderForFilter;
import com.linkyB.backend.filter.entity.GradeForFilter;
import com.linkyB.backend.filter.entity.MajorForFilter;
import com.linkyB.backend.filter.entity.MbtiForFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostFilterReq {
    private List<GradeForFilter> grade;
    private List<MajorForFilter> blockedMajor;
    private List<MbtiForFilter> blockedMbti;
    private List<GenderForFilter> gender;
}
