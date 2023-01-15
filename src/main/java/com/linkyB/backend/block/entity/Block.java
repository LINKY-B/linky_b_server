package com.linkyB.backend.block.entity;

import com.linkyB.backend.match.entity.status;
import com.linkyB.backend.match.entity.userMatchStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "userBlock")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userBlockIdx")
    private Long id;

    private Long userGiveBlock;

    private Long userGetBlocked;

    @Enumerated(EnumType.STRING)
    private userBlockStatus status;

}
