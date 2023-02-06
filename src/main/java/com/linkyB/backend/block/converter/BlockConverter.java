package com.linkyB.backend.block.converter;

import com.linkyB.backend.block.entity.Block;
import com.linkyB.backend.block.entity.BlockStatus;
import com.linkyB.backend.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class BlockConverter {
    public Block block(User userGetMatched, User userMatcing) {
        return Block.builder()
                .userGiveBlock(userGetMatched)
                .userGetBlocked(userMatcing)
                .blockStatus(BlockStatus.ACTIVE)
                .build();
    }
}
