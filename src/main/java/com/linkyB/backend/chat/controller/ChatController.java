package com.linkyB.backend.chat.controller;

import com.linkyB.backend.chat.dto.ChatMessage;
import com.linkyB.backend.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final JwtUtil jwtUtil;

    /**
     * 사용자 간 채팅 내용 전달
     */
    @CrossOrigin
    @MessageMapping("/chat/message") //websocket "/pub/chat/message"로 들어오는 메시징 처리
    public void message(ChatMessage message, @Header("Authorization") String Authorization) {

        String authorization = jwtUtil.extractJwt(Authorization);
        Object memberId = jwtUtil.parseClaims(authorization).get("memberId");
        message.setSender((String) memberId);

        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message); // /sub/chat/room/{roomId} - 구독
    }
}