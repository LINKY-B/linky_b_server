package com.linkyB.backend.filter.dto;

import com.linkyB.backend.filter.entity.GenderForFilter;
import com.linkyB.backend.filter.entity.GradeForFilter;
import com.linkyB.backend.filter.entity.MajorForFilter;
import com.linkyB.backend.filter.entity.MbtiForFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "필터 변경 요청 DTO")
public class PostFilterReq {
    @NotBlank(message = "변경이 없을 시 기존에 입력한 필터 내용이 들어와야 합니다.")
    private List<GradeForFilter> grade;
    @NotBlank(message = "변경이 없을 시 기존에 입력한 필터 내용이 들어와야 합니다.")
    private List<MajorForFilter> blockedMajor;
    @NotBlank(message = "변경이 없을 시 기존에 입력한 필터 내용이 들어와야 합니다.")
    private List<MbtiForFilter> blockedMbti;
    @NotBlank(message = "변경이 없을 시 기존에 입력한 필터 내용이 들어와야 합니다.")
    private List<GenderForFilter> gender;
}
