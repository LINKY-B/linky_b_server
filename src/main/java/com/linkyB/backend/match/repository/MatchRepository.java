package com.linkyB.backend.match.repository;

import com.linkyB.backend.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    @Modifying
    @Query("update Match m set m.userMatchStatus = 'ACTIVE' where m.userGetMatched.userId = :userId and m.status = 'ACTIVE'")
    int updateUserByUserGetMatched(@Param("userId") long userId);
}
