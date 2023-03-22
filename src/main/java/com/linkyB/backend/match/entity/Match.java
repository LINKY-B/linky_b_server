package com.linkyB.backend.match.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkyB.backend.common.entity.BaseEntity;
import com.linkyB.backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "UserMatch")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Match extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MatchId")
    private Long matchId;

    @ManyToOne
    @JoinColumn(name="userGetMatched")
    private User userGetMatched; // 연결을 당한 유저

    @ManyToOne
    @JoinColumn(name="userMatching")
    private User userMatching; // 연결을 시도한 유저

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private MatchStatus userMatchStatus; // 매칭 상태 [INACTIVE, ACTIVE]

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private status status; // 매칭 테이블 컬럼 상태 [INACTIVE, ACTIVE]

    public void update(MatchStatus userMatchStatus) {

        this.userMatchStatus = userMatchStatus;
    }
    public void updateMatch(status status) {
        this.status = status;
    }
}
