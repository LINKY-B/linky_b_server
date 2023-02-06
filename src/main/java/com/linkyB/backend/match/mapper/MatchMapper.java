package com.linkyB.backend.match.mapper;

import com.linkyB.backend.match.dto.MatchDto;
import com.linkyB.backend.match.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MatchMapper {
    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    Match dtoToEntity(MatchDto dto);

    MatchDto entityToDto(Match entity);

}
