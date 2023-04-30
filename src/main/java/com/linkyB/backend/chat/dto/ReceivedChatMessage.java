package com.linkyB.backend.chat.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class ReceivedChatMessage {

    private String roomId;
    private String message;
    private String sendingTime;
}