package com.linkyB.backend.match.repository;

import com.linkyB.backend.match.entity.Match;
import com.linkyB.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    @Modifying
    @Query("update Match m set m.userMatchStatus = 'ACTIVE' where m.userGetMatched.userId = :userId and m.status = 'ACTIVE'")
    int updateUserByUserGetMatched(@Param("userId") long userId);

    @Query("select m from Match m where m.userMatching.userId =:userMatching and m.userGetMatched.userId = :userId")
    Match findByUserMatching(@Param("userId")long userId,@Param("userMatching") long userMatching);

    @Query("select m from Match m where m.userMatching.userId =:userId and m.userGetMatched.userId = :userGetMatched")
    Match findByUserGetMatched(@Param("userId")long userId,@Param("userGetMatched") long userGetMatched);

}
