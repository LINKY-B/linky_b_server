package com.linkyB.backend.match.repository;

import com.linkyB.backend.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>  {

    @Query(value = "select m.id from Match m where m.userGetMatched = :userGetMatched and m.userMatching = :userMatching")
    Match getByUserGetMatchedAndUserMatching(@Param("userGetMatched") Long userGetMatched, @Param("userMatching") Long userMatching);
}
