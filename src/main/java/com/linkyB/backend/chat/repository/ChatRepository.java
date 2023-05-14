package com.linkyB.backend.chat.repository;

import com.linkyB.backend.chat.dto.RecentMessage;
import com.linkyB.backend.chat.dto.SavedChatMessage;
import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ChatRepository {

    private final MongoTemplate mongoTemplate;

    public void save(String messageJson, String roomId) {
        try {
            mongoTemplate.insert(messageJson, roomId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new LinkyBusinessException(ErrorCode.MESSAGE_SAVE_FAIL);
        }
    }

    public List<SavedChatMessage> getChatMessagesByRoomId(String roomId, int pageNumber, int pageSize) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "sendingTime"));
        query.skip(pageNumber * pageSize);
        query.limit(pageSize);

        List<SavedChatMessage> chatMessages = mongoTemplate.find(query, SavedChatMessage.class, roomId);

        return chatMessages;
    }

    public List<RecentMessage> getMessagesInfo(String roomId) {
        Query query = new Query();
        query.limit(1);
        query.with(Sort.by(Sort.Direction.DESC,"sendingTime"));

        return mongoTemplate.find(query, RecentMessage.class, roomId);
    }
}
