package com.linkyB.backend.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkyB.backend.chat.dto.ReceivedChatMessage;
import com.linkyB.backend.chat.dto.SavedChatMessage;
import com.linkyB.backend.chat.repository.ChatRepository;
import com.linkyB.backend.chat.repository.ChattingRoomRepository;
import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;
import com.linkyB.backend.common.util.JwtUtil;
import com.linkyB.backend.common.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final JwtUtil jwtUtil;
    private final SecurityUtils securityUtils;
    private final ChatRepository chatRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    public void saveMessage(String roomId, SavedChatMessage message) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        if (isAnyFieldEmpty(message)) {
            throw new LinkyBusinessException(ErrorCode.EMPTY_FIELD_FOUND);
        }
        String messageJson = objectMapper.writeValueAsString(message);
        chatRepository.save(messageJson, roomId);
    }

    /**
     * 저장할 메세지 필드에 빈 값이 유무 확인
     */
    private boolean isAnyFieldEmpty(SavedChatMessage message) {
        if (message.getMessage().isEmpty() ||
                message.getSender().isEmpty() ||
                message.getSendingTime().isEmpty()) {
            return true;
        }

        return false;
    }

    public String findUserId(String authorization) {
        String jwt = jwtUtil.extractJwt(authorization);
        Object memberId = jwtUtil.parseClaims(jwt).get("memberId");

        return (String) memberId;
    }

    public SavedChatMessage handleMessage(ReceivedChatMessage receivedChatMessage, String authorization) {
        String roomId = receivedChatMessage.getRoomId();
        String memberId = findUserId(authorization);

        SavedChatMessage savedChatMessage = SavedChatMessage.of(memberId, receivedChatMessage);

        try {
            saveMessage(roomId, savedChatMessage);
            messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, savedChatMessage); // /sub/chat/room/{roomId} - 구독
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new LinkyBusinessException(ErrorCode.MESSAGE_HANDLE_FAIL);
        }

        return savedChatMessage;
    }

    public List<SavedChatMessage> getChatMessagesByRoomId(String roomId, int pageNumber, int pageSize) {
        Long currentUserId = securityUtils.getCurrentUserId();
        List<List<Long>> usersInActiveChatRoom = chattingRoomRepository.findUsersInActiveChatRoom(Long.parseLong(roomId));

        if (!usersInActiveChatRoom.get(0).contains(currentUserId)) {
            throw new LinkyBusinessException(ErrorCode.CHATROOM_ACCESS_NOT_ALLOW);
        }

        return chatRepository.getChatMessagesByRoomId(roomId, pageNumber, pageSize);
    }
}
