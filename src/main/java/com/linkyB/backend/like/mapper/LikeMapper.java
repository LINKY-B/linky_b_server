package com.linkyB.backend.like.mapper;

import com.linkyB.backend.like.dto.LikeDto;
import com.linkyB.backend.like.entity.UserLikes;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface LikeMapper {
    LikeMapper INSTANCE = Mappers.getMapper(LikeMapper.class);

    UserLikes dtoToEntity(LikeDto dto);

    LikeDto entityToDto(UserLikes entity);
}
