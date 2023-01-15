package com.linkyB.backend.match.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "userMatch")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Match {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userMatchIdx")
    private Long id;

    private Long userGetMatched;

    private Long userMatching;

    @Enumerated(EnumType.STRING)
    private userMatchStatus userMatchStatus;

    @Enumerated(EnumType.STRING)
    private status status;

    public void update(userMatchStatus userMatchStatus) {

        this.userMatchStatus = userMatchStatus;
    }

    public void updateMatch(status status){
        this.status = status;
    }
}

