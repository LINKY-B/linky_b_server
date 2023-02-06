package com.linkyB.backend.user.domain;

import com.linkyB.backend.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "Personality")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Personality extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userPersonalityIdx")
    private Long id;
    @Column(name = "Personality")
    private String userPersonality;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
