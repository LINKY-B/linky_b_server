package com.linkyB.backend.home.repository;


import com.linkyB.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PagingRepository {
    private final EntityManager em;

    public List<User> findAllByGradStatusTrue(int offset, int limit, long userId, String schoolName) {
        return em.createNativeQuery(
                        "SELECT * FROM User u WHERE u.userId NOT IN (SELECT b.userGetBlocked\n" +
                                "                                FROM Block b WHERE b.blockStatus = 'ACTIVE'\n" +
                                "                                AND b.userGiveBlock =:userId)\n" +
                                "                                and u.gradeStatus = 'true' and u.SchoolName = :schoolName\n" +
                                "and :userId != u.userId", User.class)
                .setParameter("userId", userId)
                .setParameter("schoolName", schoolName)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<User> findAllByGradStatusFalse(int offset, int limit, long userId, String schoolName) {
        return em.createNativeQuery(
                        "SELECT * FROM User u WHERE u.userId NOT IN (SELECT b.userGetBlocked\n" +
                                "                                FROM Block b WHERE b.blockStatus = 'ACTIVE'\n" +
                                "                                AND b.userGiveBlock =:userId)\n" +
                                "                                and u.gradeStatus = 'false' and u.SchoolName = :schoolName\n" +
                                "and :userId != u.userId", User.class)
                .setParameter("userId" , userId)
                .setParameter("schoolName", schoolName)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

}
