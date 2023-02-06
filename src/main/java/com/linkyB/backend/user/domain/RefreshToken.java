package com.linkyB.backend.user.domain;

import com.linkyB.backend.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
public class RefreshToken extends BaseEntity {

    @Id
    @Column(name = "tokenId")
    private String userId;
    @Column(name = "value")
    private String value;

    @Builder
    public RefreshToken(String userId, String value) {
        this.userId = userId;
        this.value = value;
    }

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }
}