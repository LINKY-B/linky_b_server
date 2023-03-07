package com.linkyB.backend.like.mapper;

import com.linkyB.backend.like.dto.LikeDto;
import com.linkyB.backend.like.dto.LikeDto.LikeDtoBuilder;
import com.linkyB.backend.like.entity.UserLikes;
import com.linkyB.backend.like.entity.UserLikes.UserLikesBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-07T18:15:14+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.13 (Azul Systems, Inc.)"
)
@Component
public class LikeMapperImpl implements LikeMapper {

    @Override
    public UserLikes dtoToEntity(LikeDto dto) {
        if ( dto == null ) {
            return null;
        }

        UserLikesBuilder userLikes = UserLikes.builder();

        userLikes.likeId( dto.getLikeId() );

        return userLikes.build();
    }

    @Override
    public LikeDto entityToDto(UserLikes entity) {
        if ( entity == null ) {
            return null;
        }

        LikeDtoBuilder likeDto = LikeDto.builder();

        if ( entity.getLikeId() != null ) {
            likeDto.likeId( entity.getLikeId() );
        }

        return likeDto.build();
    }
}
