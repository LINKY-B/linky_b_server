package com.linkyB.backend.like.entity;

import com.linkyB.backend.user.domain.User;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "Likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserLikes {
    @Id
    @Column(name ="likesId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userGetLikes")
    private User userGetLikes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userGiveLikes")
    private User userGiveLikes;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;
}
