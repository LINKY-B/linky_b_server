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
@Table(name = "UserMatch",
        uniqueConstraints = @UniqueConstraint(name = "user_match_unique", columnNames = {"userGetMatched", "userMatching"})
)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Match extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MatchId")
    private Long matchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userGetMatched")
    private User userGetMatched; // 연결을 당한 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userMatching")
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

    // 연결 수락 / 거절
    public void approve(){
        this.userMatchStatus = MatchStatus.ACTIVE;
    }

    public void refuse(){
        this.userMatchStatus = MatchStatus.INACTIVE;
    }

    // 연결 삭제 / 활성화
    public void deleteMatch() {
        this.status = com.linkyB.backend.match.entity.status.INACTIVE;
    }

    public void activateMatch() {
        this.status = com.linkyB.backend.match.entity.status.ACTIVE;
    }
}
