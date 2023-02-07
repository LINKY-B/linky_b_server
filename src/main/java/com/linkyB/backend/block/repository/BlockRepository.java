package com.linkyB.backend.block.repository;

import com.linkyB.backend.block.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

    @Modifying
    @Query(value = "update Block b set b.blockStatus = 'INACTIVE' where b.blockId = :blockId")
    Integer updateStatus(@Param("blockId")long blockId);
}
