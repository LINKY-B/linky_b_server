package com.linkyB.backend.user.repository;

import com.linkyB.backend.user.domain.Interest;
import com.linkyB.backend.user.domain.Personality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserPersonalityRepository extends JpaRepository<Personality, Long> {

    @Query(value = "select p from Personality p where p.user.userId = :userId")
    List<Personality> findAllByUser(@Param("userId")long userId);

    @Modifying
    @Transactional
    @Query(value = "delete from Personality p where p.user.userId = :userId")
    void deleteAllByUserId(@Param("userId")long userId);

}
