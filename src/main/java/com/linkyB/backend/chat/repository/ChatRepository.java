package com.linkyB.backend.chat.repository;

import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

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
}
