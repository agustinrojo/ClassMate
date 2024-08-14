package com.example.chat_v1.dto.chat;

import com.example.chat_v1.dto.file.FileDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageInputDTO{
    private Long senderId;
    private Long receiverId;
    private String content;
    private FileDTO attachment;
}
