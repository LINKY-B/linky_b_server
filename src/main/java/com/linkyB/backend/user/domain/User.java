package com.linkyB.backend.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkyB.backend.common.domain.BaseEntity;
import com.linkyB.backend.filter.entity.GenderForFilter;
import com.linkyB.backend.filter.entity.GradeForFilter;
import com.linkyB.backend.filter.entity.MajorForFilter;
import com.linkyB.backend.filter.entity.MbtiForFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity(name = "User")
@DynamicInsert
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;
    @Column(name = "userPhoneNum")
    private String userPhone;
    @Column(name = "userName")
    private String userName;
    @Column(name = "userNickName")
    private String userNickName;
    @Column(name = "userBirth")
    private String userBirth;
    @Column(name = "userPassword")
    private String userPassword;
    @Column(name = "SchoolName")
    private String userSchoolName;
    @Column(name = "userMajorName")
    private String userMajorName;
    @Column(name = "userStudentNum")
    private String userStudentNum;
    @Column(name = "gradeStatus")
    private String gradStatus;
    @Column(name = "userProfileImg")
    private String userProfileImg;
    @Column(name = "userSex")
    private String userSex;
    @Column(name = "userMBTI")
    private String userMBTI;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private UserNotification userNotification; // 유저 알림 설정 상태 [INACTIVE, ACTIVE]

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private UserStatusForMyInfo userStatusForMyInfo; // 유저 정보 활성화 상태 [INACTIVE, ACTIVE]

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Personality> userPersonality;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Interest> userInterest;

    @Column(name = "userSelfIntroduction")
    private String userSelfIntroduction;


    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = LAZY)
    private List<GradeForFilter> userGradeForFilters;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = LAZY)
    private List<MajorForFilter> userMajorForFilters;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = LAZY)
    private List<MbtiForFilter> userMbtiForFilters;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = LAZY)
    private List<GenderForFilter> userGenderForFilters;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Authority authority;

    private int userLikeCount;
    public void UserLikeCount() {
        this.userLikeCount++;
    }
    public void updatePassword(String newPassword) {
        this.userPassword = newPassword;
    }

    public void updateStatusForMyInfo(UserStatusForMyInfo userStatusForMyInfo) {
        this.userStatusForMyInfo = userStatusForMyInfo;
    }

    public void updateUserNotification(UserNotification userNotification) {
        this.userNotification = userNotification;
    }
}
