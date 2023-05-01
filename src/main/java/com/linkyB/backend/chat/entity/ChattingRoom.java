package com.linkyB.backend.chat.entity;

import com.linkyB.backend.common.entity.BaseEntity;
import com.linkyB.backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "ChattingRoom")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ChattingRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ChattingRoomId")
    private Long chattingRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fromUser")
    private User fromUser; // 매칭을 수락한 사람 (== 나)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="toUser")
    private User toUser; // 나에게 매칭을 시도한 사람

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private Status status; // 매칭 테이블 컬럼 상태 [INACTIVE, ACTIVE]

    public void updateChatRoomStatus(Status status) {
        this.status = status;
    }
}
