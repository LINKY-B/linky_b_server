package com.linkyB.backend.filter.entity;

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
@Table(name = "MajorForFilter")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class MajorForFilter extends BaseEntity {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userMajorId")
    private Long userMajorId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;


    @Column(name = "blockedMajor")
    private String blockedMajor;
}
