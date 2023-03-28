package com.linkyB.backend.report.dto;

import com.linkyB.backend.match.dto.MatchDto;
import com.linkyB.backend.match.entity.Match;
import com.linkyB.backend.report.entity.Report;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class ReportDto {
    private long reportId;

    public static ReportDto of(Report report) {
        return ReportDto.builder()
                .reportId(report.getReportId())
                .build();
    }
}
