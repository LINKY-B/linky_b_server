package com.linkyB.backend.filter.repository;

import com.linkyB.backend.filter.entity.GradeForFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface GradeForFilterRepository extends JpaRepository<GradeForFilter, Long> {

    @Query(value = "select g from GradeForFilter g where g.user.userId = :userId")
    List<GradeForFilter> findAllByUser(@Param("userId") long userId);

    @Modifying
    @Transactional
    @Query(value = "delete from GradeForFilter gf where gf.user.userId = :userId")
    void deleteAllByUserId(@Param("userId") long userId);
}
