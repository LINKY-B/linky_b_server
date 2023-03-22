package com.linkyB.backend.user.dto;

import com.linkyB.backend.user.domain.Interest;
import com.linkyB.backend.user.domain.Personality;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "프로필 수정 요청 DTO")
public class PatchUserReq {

    @NotBlank(message = "전공이름은 필수 영역입니다.")
    private String userMajorName;

    @NotNull(message = "MBTI에 null이 들어올 수 없습니다. 필요한 경우 빈 문자열로 대체해주십시오.")
    private String userMbti;

    @NotNull(message = "소개글에 null이 들어올 수 없습니다. 필요한 경우 빈 문자열로 대체해주십시오.")
    private String userSelfIntroduction;

    private List<String> userInterests = new ArrayList<>();
    private List<String> userPersonalities = new ArrayList<>();

}
