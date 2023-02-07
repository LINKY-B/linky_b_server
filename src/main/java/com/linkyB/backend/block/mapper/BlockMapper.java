package com.linkyB.backend.block.mapper;

import com.linkyB.backend.block.dto.BlockDto;
import com.linkyB.backend.block.entity.Block;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BlockMapper {
    BlockMapper INSTANCE = Mappers.getMapper(BlockMapper.class);

    Block dtoToEntity(BlockDto dto);

    BlockDto entityToDto(Block entity);

}
