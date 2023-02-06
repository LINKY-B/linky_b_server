package com.linkyB.backend.report.dto;

import com.linkyB.backend.report.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostReportReq {
    private String reportDetail;

    public String getReportDetail() {
        return reportDetail;
    }

}
