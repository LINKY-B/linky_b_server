package com.linkyB.backend.report.mapper;

import com.linkyB.backend.report.dto.ReportDto;
import com.linkyB.backend.report.dto.ReportDto.ReportDtoBuilder;
import com.linkyB.backend.report.entity.Report;
import com.linkyB.backend.report.entity.Report.ReportBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-15T03:09:38+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.18 (Azul Systems, Inc.)"
)
@Component
public class ReportMapperImpl implements ReportMapper {

    @Override
    public Report dtoToEntity(ReportDto dto) {
        if ( dto == null ) {
            return null;
        }

        ReportBuilder report = Report.builder();

        report.reportId( dto.getReportId() );

        return report.build();
    }

    @Override
    public ReportDto entityToDto(Report entity) {
        if ( entity == null ) {
            return null;
        }

        ReportDtoBuilder reportDto = ReportDto.builder();

        if ( entity.getReportId() != null ) {
            reportDto.reportId( entity.getReportId() );
        }

        return reportDto.build();
    }
}
