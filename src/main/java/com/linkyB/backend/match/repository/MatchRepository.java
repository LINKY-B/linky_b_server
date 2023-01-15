package com.linkyB.backend.match.repository;

import com.linkyB.backend.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>  {

    @Modifying
    @Query("update Match m set m.userMatchStatus = 'ACTIVE' where m.userGetMatched = :userGetMatched")
    int updateMatchStatusByUserGetMatched(@Param("userGetMatched") Long userGetMatched);

}
