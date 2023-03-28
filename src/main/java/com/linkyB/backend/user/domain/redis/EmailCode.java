package com.linkyB.backend.user.domain.redis;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@RedisHash("email_codes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailCode {
    @Id
    @Indexed
    private String id;

    @Indexed
    private String email;

    @Indexed
    private String userNickName;

    private String code;

    private LocalDateTime lastUpdateDate;

    @TimeToLive(unit = TimeUnit.HOURS)
    private Long timeout = 2L;

    @Builder
    public EmailCode(String userNickName, String email, String code) {
        this.userNickName = userNickName;
        this.email = email;
        this.code = code;
        this.lastUpdateDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
