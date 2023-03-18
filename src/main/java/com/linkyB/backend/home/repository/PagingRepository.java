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

    public List<User> findAllByGradStatusTrue(int offset, int limit, long userId) {
        return em.createNativeQuery(
                        "SELECT * FROM User u WHERE u.userId NOT IN (SELECT b.userGetBlocked " +
                                "FROM Block b WHERE b.blockStatus = 'ACTIVE'" +
                                "AND b.userGiveBlock =:userId )" +
                                "AND u.gradeStatus = 'true' ", User.class)
                .setParameter("userId", userId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<User> findAllByGradStatusFalse(int offset, int limit, long userId) {
        return em.createNativeQuery(
                        "SELECT * FROM User u WHERE u.userId NOT IN (SELECT b.userGetBlocked " +
                                "FROM Block b WHERE b.blockStatus = 'ACTIVE'" +
                                "AND b.userGiveBlock =:userId )" +
                                "AND u.gradeStatus = 'false' ", User.class)
                .setParameter("userId" , userId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

}
