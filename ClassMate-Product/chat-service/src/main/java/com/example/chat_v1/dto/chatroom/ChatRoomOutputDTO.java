package com.example.chat_v1.dto.chatroom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomOutputDTO {
    private Long id;
    private String chatId;
    private Long senderId;
    private Long receiverId;
}
