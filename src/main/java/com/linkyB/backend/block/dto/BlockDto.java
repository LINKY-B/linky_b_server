package com.linkyB.backend.block.dto;

import com.linkyB.backend.block.entity.Block;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class BlockDto {
    private long blockId;

    public static BlockDto of(Block block) {
        return BlockDto.builder()
                .blockId(block.getBlockId())
                .build();
    }
}
