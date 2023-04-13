package com.linkyB.backend.user.dto;

import com.linkyB.backend.user.domain.Interest;
import com.linkyB.backend.user.domain.Personality;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "프로필 수정 요청 DTO")
public class PatchUserReq {

    @NotNull(message = "MBTI에 null이 들어올 수 없습니다. 필요한 경우 빈 문자열로 대체해주십시오.")
    private String userMbti;

    @NotNull(message = "소개글에 null이 들어올 수 없습니다. 필요한 경우 빈 문자열로 대체해주십시오.")
    private String userSelfIntroduction;

    @ApiModelProperty(value = "프로필 이미지", example = "https://linkyb-bucket.s3.ap-northeast-2.amazonaws.com/images/profileImg/M11.svg")
    @URL(message = "URL 형식이 아닙니다.")
    private String profileImg;

    private List<String> userInterests = new ArrayList<>();
    private List<String> userPersonalities = new ArrayList<>();

}
