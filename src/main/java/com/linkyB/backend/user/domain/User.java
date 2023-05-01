package com.linkyB.backend.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkyB.backend.block.entity.Block;
import com.linkyB.backend.chat.entity.ChattingRoom;
import com.linkyB.backend.common.entity.BaseEntity;
import com.linkyB.backend.filter.entity.GenderForFilter;
import com.linkyB.backend.filter.entity.GradeForFilter;
import com.linkyB.backend.filter.entity.MajorForFilter;
import com.linkyB.backend.filter.entity.MbtiForFilter;
import com.linkyB.backend.like.entity.UserLikes;
import com.linkyB.backend.match.entity.Match;
import com.linkyB.backend.report.entity.Report;
import com.linkyB.backend.user.domain.enums.ActiveStatus;
import com.linkyB.backend.user.domain.enums.Authority;
import com.linkyB.backend.user.domain.enums.UserNotification;
import com.linkyB.backend.user.domain.enums.UserStatusForMyInfo;
import com.linkyB.backend.user.dto.PatchUserReq;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity(name = "User")
@DynamicInsert
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @Column(name = "userEmail", nullable = false, unique = true)

    private String userEmail;

    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name = "userNickName", nullable = false, unique = true)
    private String userNickName;

    @Column(name = "userBirth")
    private String userBirth;

    @Column(name = "userPassword", nullable = false)
    private String userPassword;

    @Column(name = "SchoolName")
    private String userSchoolName;

    @Column(name = "userMajorName")
    private String userMajorName;

    @Column(name = "userStudentNum")
    private String userStudentNum;

    @Column(name = "gradeStatus")
    private String gradeStatus;

    @Column(name = "userProfileImg")
    private String userProfileImg;

    @Column(name = "userSchoolImg")
    private String userSchoolImg;

    @Column(name = "userSex")
    private String userSex;

    @Column(name = "userMBTI")
    private String userMBTI;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Authority authority;

    private int userLikeCount;
    private int userMatchingCount;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private UserNotification userNotification; // 유저 알림 설정 상태 [INACTIVE, ACTIVE]

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private UserStatusForMyInfo userStatusForMyInfo; // 유저 정보 활성화 상태 [INACTIVE, ACTIVE]

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private ActiveStatus isActive; // 유저 삭제 여부


    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Personality> userPersonality = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interest> userInterest = new ArrayList<>();


    @Column(name = "userSelfIntroduction")
    private String userSelfIntroduction;


    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<GradeForFilter> userGradeForFilters = new ArrayList<>();


    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MajorForFilter> userMajorForFilters = new ArrayList<>();


    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MbtiForFilter> userMbtiForFilters = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<GenderForFilter> userGenderForFilters = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userReport", cascade = CascadeType.ALL)
    private List<Report> report = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userGetReported", cascade = CascadeType.ALL)
    private List<Report> Getreported = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userGetLikes", cascade = CascadeType.ALL)
    private List<UserLikes> userGetLikes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userGiveLikes", cascade = CascadeType.ALL)
    private List<UserLikes> userGiveLikes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userGiveBlock", cascade = CascadeType.ALL)
    private List<Block> userGiveBlock = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userGetBlocked", cascade = CascadeType.ALL)
    private List<Block> userGetBlocked = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL)
    private List<ChattingRoom> fromUser = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL)
    private List<ChattingRoom> toUser = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userGetMatched", cascade = CascadeType.ALL)
    private List<Match> userGetMatched = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userMatching", cascade = CascadeType.ALL)
    private List<Match> userMatching = new ArrayList<>();

    // == 생성메서드
    @Builder
    public User(String userEmail, String userPassword, String userName, String userNickName,
                String userBirth, String userSchoolName, String userMajorName, String userProfileImg,
                String userSchoolImg, String gradeStatus, String userMBTI, String userStudentNum,
                String userSelfIntroduction, String userSex) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userNickName = userNickName;
        this.userBirth = userBirth;
        this.userSchoolName = userSchoolName;
        this.userMajorName = userMajorName;
        this.userProfileImg = userProfileImg;
        this.userSchoolImg = userSchoolImg;
        this.gradeStatus = gradeStatus;
        this.userMBTI = userMBTI;
        this.userStudentNum = userStudentNum;
        this.userSelfIntroduction = userSelfIntroduction;
        this.userSex = userSex;
        this.authority = Authority.USER;
    }


    // == 상태 변경 메서드 ==
    public void increaseLikeCount() {
        this.userLikeCount++;
    }

    public void increaseMatchCount() {
        this.userMatchingCount++;
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

    public void updateInfo(PatchUserReq dto) {
        this.userMBTI = dto.getUserMbti();
        this.userSelfIntroduction = dto.getUserSelfIntroduction();
        this.userProfileImg = dto.getProfileImg();

        // 기존 interest, personality 다 날리고 새로 저장.
        this.userInterest.clear();
        this.userPersonality.clear();
        dto.getUserInterests().stream().map(Interest::new).forEach(i -> this.addInterests(i));
        dto.getUserPersonalities().stream().map(Personality::new).forEach(p -> this.addPersonality(p));
    }

    public void deleteUser() {
        this.isActive = ActiveStatus.INACTIVE;
    }

    public void restoreUser(){
        this.isActive = ActiveStatus.ACTIVE;
    }

    // == 연관관계 메서드 ==
    public void addInterests(Interest interest) {
        this.userInterest.add(interest);
        interest.setUser(this);
    }

    public void addPersonality(Personality personality) {
        userPersonality.add(personality);
        personality.setUser(this);
    }
}
