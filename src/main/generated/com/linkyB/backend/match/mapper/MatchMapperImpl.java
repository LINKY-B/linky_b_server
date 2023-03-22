package com.linkyB.backend.match.mapper;

import com.linkyB.backend.match.dto.MatchDto;
import com.linkyB.backend.match.dto.MatchDto.MatchDtoBuilder;
import com.linkyB.backend.match.entity.Match;
import com.linkyB.backend.match.entity.Match.MatchBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-22T04:04:21+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.18 (Azul Systems, Inc.)"
)
@Component
public class MatchMapperImpl implements MatchMapper {

    @Override
    public Match dtoToEntity(MatchDto dto) {
        if ( dto == null ) {
            return null;
        }

        MatchBuilder match = Match.builder();

        match.matchId( dto.getMatchId() );

        return match.build();
    }

    @Override
    public MatchDto entityToDto(Match entity) {
        if ( entity == null ) {
            return null;
        }

        MatchDtoBuilder matchDto = MatchDto.builder();

        if ( entity.getMatchId() != null ) {
            matchDto.matchId( entity.getMatchId() );
        }

        return matchDto.build();
    }
}
