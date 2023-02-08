package com.linkyB.backend.chat.converter;

import com.linkyB.backend.chat.entity.ChattingRoom;
import com.linkyB.backend.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class ChattingConverter {
    public ChattingRoom createChat(final User toUser, final User fromUser) {
        return ChattingRoom.builder()
                .toUser(toUser)
                .fromUser(fromUser)
                .build();
    }
}
