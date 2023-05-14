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
     * 추후 더 좋은 쿼리문과 NOT IN은 성능이 좋지 않음... QueryDsl로 리팩토링하기.
     */
    public List<User> findAll(int offset, int limit, long userId, String schoolName, boolean isGraduate) {
        String graduate = isGraduate ? "true" : "false";

                return em.createNativeQuery(
            "SELECT * FROM User u " +
                    "WHERE u.userId NOT IN (SELECT b.userGetBlocked FROM Block b WHERE b.blockStatus = 'ACTIVE' AND b.userGiveBlock = :userId) " +
                    "        and u.userId NOT IN(SELECT m.userMatching FROM `UserMatch` m where (userGetMatched = :userId) and`status` = 'ACTIVE') " +
                    "        and u.userId NOT IN(SELECT m.userGetMatched FROM `UserMatch` m where (userMatching = :userId) and`status` = 'ACTIVE') " +
                    "        and u.gradeStatus = :gradeStatus and u.SchoolName = :schoolName and u.userId != :userId", User.class)
                .setParameter("userId", userId)
                .setParameter("schoolName", schoolName)
                .setParameter("gradeStatus", graduate)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

    }

}
