package com.linkyB.backend.block.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatchBlockReq {
    private List<Long> blockId;
}
