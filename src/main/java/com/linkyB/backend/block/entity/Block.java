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
@Table(name = "Block")
@SuperBuilder
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blockId")
    private Long blockId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userGiveBlock")
    private User userGiveBlock;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userGetBlocked")
    private User userGetBlocked;

    @Enumerated(EnumType.STRING)
    private BlockStatus blockStatus;

}

