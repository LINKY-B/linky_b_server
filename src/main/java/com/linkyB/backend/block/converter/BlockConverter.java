package com.linkyB.backend.block.converter;

import com.linkyB.backend.block.entity.Block;
import com.linkyB.backend.block.entity.BlockStatus;
import com.linkyB.backend.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class BlockConverter {
    public Block block(User userGetMatched, User userMatching) {
        return Block.builder()
                .userGiveBlock(userGetMatched)
                .userGetBlocked(userMatching)
                .blockStatus(BlockStatus.ACTIVE)
                .build();
    }

    public Block blockGetMatched(User userMatching, User userGetMatched) {
        return Block.builder()
                .userGiveBlock(userMatching)
                .userGetBlocked(userGetMatched)
                .blockStatus(BlockStatus.ACTIVE)
                .build();
    }
}
