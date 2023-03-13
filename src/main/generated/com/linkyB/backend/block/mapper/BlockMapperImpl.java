package com.linkyB.backend.block.mapper;

import com.linkyB.backend.block.dto.BlockDto;
import com.linkyB.backend.block.dto.BlockDto.BlockDtoBuilder;
import com.linkyB.backend.block.entity.Block;
import com.linkyB.backend.block.entity.Block.BlockBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-11T14:17:08+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.13 (Azul Systems, Inc.)"
)
@Component
public class BlockMapperImpl implements BlockMapper {

    @Override
    public Block dtoToEntity(BlockDto dto) {
        if ( dto == null ) {
            return null;
        }

        BlockBuilder<?, ?> block = Block.builder();

        block.blockId( dto.getBlockId() );

        return block.build();
    }

    @Override
    public BlockDto entityToDto(Block entity) {
        if ( entity == null ) {
            return null;
        }

        BlockDtoBuilder blockDto = BlockDto.builder();

        if ( entity.getBlockId() != null ) {
            blockDto.blockId( entity.getBlockId() );
        }

        return blockDto.build();
    }
}
