package com.linkyB.backend.user.repository;

import com.linkyB.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserPhone(String userPhone);
    boolean existsByUserPhone(String userPhone);

    @Query(value = "SELECT u FROM User u JOIN Match m ON m.userMatching.userId = u.userId " +
            "WHERE m.userGetMatched.userId = :userId AND m.status = 'ACTIVE' AND m.userMatchStatus = 'INACTIVE'")
    List<User> findAllByUserGetMatched(@Param("userId") long userId);

    @Query(value = "SELECT u FROM User u JOIN Match m ON m.userGetMatched.userId = u.userId " +
            "WHERE m.userMatching.userId = :userId AND m.status = 'ACTIVE' AND m.userMatchStatus = 'INACTIVE'")
    List<User> findAllByUserMatching(@Param("userId") long userId);

    @Query(value = "SELECT u FROM User u JOIN Match m ON m.userMatching.userId = u.userId " +
            "WHERE m.userGetMatched.userId = :userId AND m.status = 'ACTIVE' AND m.userMatchStatus = 'INACTIVE'")
    List<User> findTop4ByUserGetMatched(@Param("userId") long userId);

    @Query(value = "SELECT u FROM User u JOIN Match m ON m.userGetMatched.userId = u.userId " +
            "WHERE m.userMatching.userId = :userId AND m.status = 'ACTIVE' AND m.userMatchStatus = 'INACTIVE'")
    List<User> findTop4ByUserMatching(@Param("userId") long userId);
}
