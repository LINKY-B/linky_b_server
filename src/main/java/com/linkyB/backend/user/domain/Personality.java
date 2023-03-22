package com.linkyB.backend.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkyB.backend.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity(name = "Personality")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Personality extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userPersonalityIdx")
    private Long id;
    @Column(name = "Personality")
    private String userPersonality;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    // Constructors
    public Personality(String userPersonality) {
        this.userPersonality = userPersonality;
    }

    public Personality(User user, String userPersonality) {
        this.user = user;
        this.userPersonality = userPersonality;
    }

    // 생성메서드
    public static Personality of(String userPersonality) {
        return new Personality(userPersonality);
    }

    public static Personality of(User user, String userPersonality) {
        return new Personality(user, userPersonality);
    }

    // 연관관계 메서드
    public void setUser(User user) {
        this.user = user;
    }
}
