package com.linkyB.backend.chat.controller;

import com.linkyB.backend.chat.dto.ReceivedChatMessage;
import com.linkyB.backend.chat.dto.RecentMessage;
import com.linkyB.backend.chat.dto.SavedChatMessage;
import com.linkyB.backend.chat.service.ChatService;
import com.linkyB.backend.common.result.ResultResponse;
import com.linkyB.backend.common.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.linkyB.backend.common.result.ResultCode.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * 사용자 간 채팅 내용 전달
     */
    @CrossOrigin
    @MessageMapping("/chat/message") //websocket "/pub/chat/message"로 들어오는 메시징 처리
    public ResultResponse<SavedChatMessage> message(ReceivedChatMessage receivedChatMessage, @Header("Authorization") String Authorization)  {

        SavedChatMessage savedChatMessage = chatService.handleMessage(receivedChatMessage, Authorization);
        return new ResultResponse<>(MESSAGE_HANDLING_SUCCESS, savedChatMessage);
    }

    /**
     * 채팅방 대화 내용 출력
     */
    @GetMapping("/messages")
    public ResultResponse<List<SavedChatMessage>> getChatMessages(@RequestParam String roomId,
                                                  @RequestParam(defaultValue = "0") int pageNumber,
                                                  @RequestParam(defaultValue = "20") int pageSize) {

        List<SavedChatMessage> response = chatService.getChatMessagesByRoomId(roomId, pageNumber, pageSize);
        return new ResultResponse<>(GET_MESSAGE_LIST_SUCCESS, response);
    }

    /**
     * 사용자별 대화 중인 채팅방 리스트 조회
     */
    @GetMapping("/message-info")
    public ResultResponse<List<RecentMessage>> getMessagesInfo() {

        List<Integer> values = chatService.getChatRooms(SecurityUtils.getCurrentUserId());
        List<RecentMessage> response = chatService.getMessagesInfo(values); // 포멧 수정 필요
        return new ResultResponse<>(GET_MESSAGE_INFO_LIST_SUCCESS, response);
    }

}