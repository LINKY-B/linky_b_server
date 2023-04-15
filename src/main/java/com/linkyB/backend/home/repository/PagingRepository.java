package com.linkyB.backend.home.repository;


import com.linkyB.backend.match.entity.status;
import com.linkyB.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PagingRepository {
    private final EntityManager em;

    /**
     * 현재 사용자와 '연결내역'이 존재하지 않고, '차단되지 않은' 같은 학교 사용자 리스트
     * 추후 QueryDsl로 리팩토링하기.
     */
    public List<User> findAll(int offset, int limit, long userId, String schoolName, boolean isGraduate) {
        String graduate = isGraduate ? "true" : "false";

        return em.createNativeQuery(
            "SELECT * FROM `User` u" +
                    "    LEFT JOIN `Block` b on u.userId = b.userGetBlocked" +
                    "    LEFT JOIN `UserMatch` m on (m.userMatching = u.userId or m.userGetMatched = u.userId)" +
                    "    where ((b.userGiveBlock is null) or (b.userGiveBlock != :userId or b.blockStatus = 'INACTIVE')) and" +
                    "    ((m.status is null or m.status = 'INACTIVE') or (m.userMatching != :userId and m.userGetMatched != :userId)) and" +
                    "    (u.SchoolName = :schoolName and u.userId != :userId and u.gradeStatus = :gradeStatus)", User.class)
                .setParameter("userId", userId)
                .setParameter("schoolName", schoolName)
                .setParameter("gradeStatus", graduate)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

}
