package com.linkyB.backend.match.repository;

import com.linkyB.backend.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    /**
     * update문으로 쿼리를 날리면 영속성 컨텍스트 캐시의 내용과 불일치가 일어나므로 캐시를 모두 날린다.
     * 이렇게 쿼리로 한번에 날리고 캐시를 날리는 것이, jpa가 기본으로 제공하는 변경감지보다 더 효과적인지에 대한 조사가 필요할 것 같다.
     */
    @Modifying(clearAutomatically = true)
    @Query("update Match m set m.userMatchStatus = 'ACTIVE' where m.userGetMatched.userId = :userId and m.status = 'ACTIVE'")
    int updateUserByUserGetMatched(@Param("userId") long userId);


    @Query("select m from Match m where m.userMatching.userId =:tryMatchingUserId and m.userGetMatched.userId = :getMatchedUserId")
    Optional<Match> findMatchByTryMatchingUserAndMatchedUser(
            @Param("tryMatchingUserId") long tryMatchingUserId,
            @Param("getMatchedUserId") long getMatchedUserId
    );

}
