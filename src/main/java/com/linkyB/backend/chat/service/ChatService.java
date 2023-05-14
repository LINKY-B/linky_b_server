package com.linkyB.backend.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkyB.backend.chat.dto.ReceivedChatMessage;
import com.linkyB.backend.chat.dto.RecentMessage;
import com.linkyB.backend.chat.dto.SavedChatMessage;
import com.linkyB.backend.chat.entity.ChattingRoom;
import com.linkyB.backend.chat.entity.Status;
import com.linkyB.backend.chat.repository.ChatRepository;
import com.linkyB.backend.chat.repository.ChattingRoomRepository;
import com.linkyB.backend.common.exception.ErrorCode;
import com.linkyB.backend.common.exception.LinkyBusinessException;
import com.linkyB.backend.common.util.JwtUtil;
import com.linkyB.backend.common.util.SecurityUtils;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final JwtUtil jwtUtil;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
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

    /**
     * 메세지 처리(채팅 내용 저장 성공 시 채팅 내용 전달)
     */
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
        Long currentUserId = SecurityUtils.getCurrentUserId();
        List<List<Long>> usersInActiveChatRoom = chattingRoomRepository.findUsersInActiveChatRoom(Long.parseLong(roomId));

        if (!usersInActiveChatRoom.get(0).contains(currentUserId)) {
            throw new LinkyBusinessException(ErrorCode.CHATROOM_ACCESS_NOT_ALLOW);
        }

        return chatRepository.getChatMessagesByRoomId(roomId, pageNumber, pageSize);
    }

    public List<Integer> getChatRooms(Long currentUserId) {
        return chattingRoomRepository.findAllActiveChatRoomByUserId(currentUserId);
    }

    /**
     * 채팅방별 최신 내역 조회
     */
    public List<RecentMessage> getMessagesInfo(List<Integer> values) {
        List<RecentMessage> chatMessages = new ArrayList<>();
        for (Integer i: values) {
            List<RecentMessage> tempMessage = chatRepository.getMessagesInfo(Integer.toString(i));
            if(!tempMessage.isEmpty()) {
                tempMessage.get(0).setRoomId(Integer.toString(i)); //chatRepository.getMessagesInfo의 반환형이 리스트이기 때문에 인덱스로 접근
                chatMessages.add(tempMessage.get(0));
            }
        }
        chatMessages.sort(Comparator.comparing(RecentMessage::getSendingTime).reversed());

        return getAdditionalMessagesInfo(chatMessages);
    }

    /**
     * 조회한 최신 내역에서 상세 내역 추가
     */
    private List<RecentMessage> getAdditionalMessagesInfo(List<RecentMessage> chatMessages) {
        List<RecentMessage> recentMessages = new ArrayList<>();
        for (RecentMessage message : chatMessages) {
            long userId = Long.parseLong(message.getSender());
            Optional<User> userInfo = userRepository.findById(userId);

            RecentMessage recntMessage = RecentMessage.builder()
                    .roomId(message.getRoomId())
                    .profileImg(userInfo.get().getUserProfileImg())
                    .nickname(userInfo.get().getUserNickName())
                    .sender(message.getSender())
                    .major(userInfo.get().getUserMajorName())
                    .undergradYear(userInfo.get().getUserStudentNum() + "학번")
                    .message(message.getMessage())
                    .sendingTime(message.getSendingTime())
                    .build();

            recentMessages.add(recntMessage);
        }
        return recentMessages;
    }

    public void exit(long roomId) {
        Optional<ChattingRoom> optionalChattingRoom = chattingRoomRepository.findById(roomId);
        if (optionalChattingRoom.isPresent()) {
            ChattingRoom chattingRoom = optionalChattingRoom.get();
            chattingRoom.updateChatRoomStatus(Status.INACTIVE);
            chattingRoomRepository.save(chattingRoom);
        } else {
            throw new LinkyBusinessException(ErrorCode.CHATROOM_NOT_FOUND);
        }
    }
}
