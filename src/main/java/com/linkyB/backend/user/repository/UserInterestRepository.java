package com.linkyB.backend.user.repository;

import com.linkyB.backend.user.domain.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserInterestRepository extends JpaRepository<Interest, Long> {

    @Query(value = "select i from Interest i where i.user.userId = :userId")
    List<Interest> findAllByUser(@Param("userId")long userId);

    @Modifying
    @Transactional
    @Query(value = "delete from Interest i where i.user.userId = :userId")
    void deleteAllByUserId(@Param("userId")long userId);
}
