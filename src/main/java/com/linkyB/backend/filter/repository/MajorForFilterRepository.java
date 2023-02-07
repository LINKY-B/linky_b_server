package com.linkyB.backend.filter.repository;

import com.linkyB.backend.filter.entity.MajorForFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MajorForFilterRepository extends JpaRepository<MajorForFilter, Long> {

    @Query(value = "select mj from MajorForFilter mj where mj.user.userId = :userId")
    List<MajorForFilter> findAllByUser(@Param("userId") long userId);

    @Modifying
    @Transactional
    @Query(value = "delete from MajorForFilter jf where jf.user.userId = :userId")
    void deleteAllByUserId(@Param("userId") long userId);
}

