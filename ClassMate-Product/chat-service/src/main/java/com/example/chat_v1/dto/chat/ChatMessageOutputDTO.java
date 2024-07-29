package com.example.chat_v1.dto.chat;

import lombok.*;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageOutputDTO {
    private Long senderId;
    private Long receiverId;
    private String chatId;
    private String content;
    private Date timeStamp;
}
