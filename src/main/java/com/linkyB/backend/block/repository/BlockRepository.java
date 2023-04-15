package com.linkyB.backend.block.repository;

import com.linkyB.backend.block.entity.Block;
import com.linkyB.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

    // 한 사람에게 차단당한 사람들의 리스트를 가져온다.
    @Query(value = "select b from Block b where b.userGiveBlock.userId = :giveBlockUserId and b.userGetBlocked.userId in :targetUsers")
    List<Block> getBlocks(@Param("giveBlockUserId") Long giveBlockUserId, @Param("targetUsers") List<Long> targetUsers);


    // 차단한 사람과 차단당한 사람으로 조회
    Optional<Block> findByUserGiveBlockAndUserGetBlocked(User userGiveBlockId, User userGetBlockId);
}
