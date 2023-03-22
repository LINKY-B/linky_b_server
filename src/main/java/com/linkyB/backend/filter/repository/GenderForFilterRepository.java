package com.linkyB.backend.filter.repository;

import com.linkyB.backend.filter.entity.GenderForFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface GenderForFilterRepository extends JpaRepository<GenderForFilter, Long> {

    @Query(value = "select f from GenderForFilter f where f.user.userId = :userId")
    List<GenderForFilter> findAllByUser(@Param("userId") long userId);

    @Modifying
    @Transactional
    @Query(value = "delete from GenderForFilter f where f.user.userId = :userId")
    void deleteAllByUserId(@Param("userId") long userId);
}
