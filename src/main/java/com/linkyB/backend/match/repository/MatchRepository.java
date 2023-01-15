package com.linkyB.backend.match.repository;

import com.linkyB.backend.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>  {
}
