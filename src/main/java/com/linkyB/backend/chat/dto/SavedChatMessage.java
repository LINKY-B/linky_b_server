package com.linkyB.backend.chat.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class SavedChatMessage {

    private String sender;
    private String message;
    private String sendingTime;

    public static SavedChatMessage of(String memberId, ReceivedChatMessage receivedChatMessage) {
        return SavedChatMessage.builder()
                .sender(memberId)
                .message(receivedChatMessage.getMessage())
                .sendingTime(receivedChatMessage.getSendingTime())
                .build();
    }
}