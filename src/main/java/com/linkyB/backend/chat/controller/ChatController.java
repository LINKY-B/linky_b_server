package com.linkyB.backend.chat.controller;

import com.linkyB.backend.chat.dto.ReceivedChatMessage;
import com.linkyB.backend.chat.dto.SavedChatMessage;
import com.linkyB.backend.chat.service.ChatService;
import com.linkyB.backend.common.result.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import static com.linkyB.backend.common.result.ResultCode.MESSAGE_HANDLING_SUCCESS;

@RequiredArgsConstructor
@Controller
@Slf4j
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
}