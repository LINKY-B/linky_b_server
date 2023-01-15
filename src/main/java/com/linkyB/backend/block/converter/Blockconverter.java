package com.linkyB.backend.block.converter;

import com.linkyB.backend.block.entity.Block;
import com.linkyB.backend.block.entity.userBlockStatus;
import com.linkyB.backend.match.dto.MatchNoResDto;
import org.springframework.stereotype.Component;

@Component
public class Blockconverter {

    public Block ReqMatchNoDto(Long userGetMatched, Long userMatcing) {
        return Block.builder()
                .userGiveBlock(userGetMatched)
                .userGetBlocked(userMatcing)
                .status(userBlockStatus.ACTIVE)
                .build();
    }

    public MatchNoResDto ResMatchNoDto(Block block){
        return MatchNoResDto.builder()
                .id(block.getId())
                .build();
    }

    public Block ReqMatchdeleteDto(Long userMatching, Long userGetMatched) {
        return Block.builder()
                .userGiveBlock(userMatching)
                .userGetBlocked(userGetMatched)
                .status(userBlockStatus.ACTIVE)
                .build();
    }
}
