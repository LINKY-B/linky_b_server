package com.linkyB.backend.block.entity;


import com.linkyB.backend.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "Block",
        uniqueConstraints = @UniqueConstraint(name = "user_block_unique", columnNames = {"userGiveBlock", "userGetBlocked"})
)
@SuperBuilder
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blockId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userGiveBlock")
    private User userGiveBlock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userGetBlocked")
    private User userGetBlocked;

    @Enumerated(EnumType.STRING)
    private BlockStatus blockStatus;

    // 상태변경 메서드
    public void setActive(boolean isActive) {
        if (isActive) {
            this.blockStatus = BlockStatus.ACTIVE;
        } else {
            this.blockStatus = BlockStatus.INACTIVE;
        }
    }

}

