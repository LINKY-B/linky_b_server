package com.linkyB.backend.block.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class BlockDto {
    private long blockId;
}
