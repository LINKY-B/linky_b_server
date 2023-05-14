package com.linkyB.backend.chat.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class RecentMessage {

    private String roomId;
    private String profileImg;
    private String nickname;
    private String sender;
    private String major;
    private String undergradYear;
    private String message;
    private String sendingTime;
}
