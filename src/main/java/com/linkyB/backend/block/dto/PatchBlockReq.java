package com.linkyB.backend.block.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "차단 해제할 유저 리스트 요청 DTO")
public class PatchBlockReq {

    private List<Long> blockId;
}
