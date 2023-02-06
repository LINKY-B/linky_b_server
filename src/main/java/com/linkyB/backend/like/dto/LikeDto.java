package com.linkyB.backend.like.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class LikeDto {
    private long likeId;
}
