package com.linkyB.backend.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkyB.backend.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity(name = "Interest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Interest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userInterestIdx")
    private Long id;

    @Column(name = "Interest")
    private String userInterest;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    // Constructors
    public Interest(String userInterest){
        this.userInterest = userInterest;
    }

    public Interest(User user, String userInterest){
        this.user = user;
        this.userInterest = userInterest;
    }

    // 생성메서드
    public static Interest of(String userInterest){
        return new Interest(userInterest);
    }
    public static Interest of(User user, String userInterest){
        return new Interest(user, userInterest);
    }

    // 연관관계 메서드
    public void setUser(User user) {
        this.user = user;
    }
}