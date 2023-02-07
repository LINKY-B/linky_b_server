package com.linkyB.backend.report.converter;

import com.linkyB.backend.report.dto.PostReportReq;
import com.linkyB.backend.report.entity.Report;
import com.linkyB.backend.report.entity.ReportStatus;
import com.linkyB.backend.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class ReportConverter {
    public Report Report(final User userReport, final User userGetReported, final PostReportReq dto) {
        return Report.builder()
                .userReport(userReport)
                .userGetReported(userGetReported)
                .reportDetail(dto.getReportDetail())
                .status(ReportStatus.ACTIVE)
                .build();
    }
}
