package com.linkyB.backend.report.mapper;

import com.linkyB.backend.report.dto.ReportDto;
import com.linkyB.backend.report.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface ReportMapper {
    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);

    Report dtoToEntity(ReportDto dto);

    ReportDto entityToDto(Report entity);
}
