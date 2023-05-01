package com.linkyB.backend.chat.repository;

import com.linkyB.backend.chat.entity.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

    @Query(value = "SELECT c.fromUser.userId, c.toUser.userId FROM ChattingRoom c WHERE c.chattingRoomId = :ChattingRoomId AND c.status = 'ACTIVE'")
    List<List<Long>> findUsersInActiveChatRoom(@Param("ChattingRoomId") Long ChattingRoomId);
}
