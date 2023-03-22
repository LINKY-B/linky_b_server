package com.linkyB.backend.report.dto;

import com.linkyB.backend.report.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostReportReq {
    @NotBlank(message = "신고 사유는 필수 입력란입니다")
    private String reportDetail;

    public String getReportDetail() {
        return reportDetail;
    }

}
