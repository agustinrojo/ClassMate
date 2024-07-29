package com.example.chat_v1.service.mapper;

import com.example.chat_v1.dto.chat.ChatMessageInputDTO;
import com.example.chat_v1.dto.chat.ChatMessageOutputDTO;
import com.example.chat_v1.entity.ChatMessage;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageMapper {

    public ChatMessage mapInputDTOToChatMessage(ChatMessageInputDTO chatMessageInputDTO){
        return ChatMessage.builder()
                .senderId(chatMessageInputDTO.getSenderId())
                .receiverId(chatMessageInputDTO.getReceiverId())
                .content(chatMessageInputDTO.getContent())
                .build();
    }

    public ChatMessageOutputDTO mapChatMessageToOutputDTO(ChatMessage chatMessage){
        return ChatMessageOutputDTO.builder()
                .senderId(chatMessage.getSenderId())
                .receiverId(chatMessage.getReceiverId())
                .chatId(chatMessage.getChatId())
                .content(chatMessage.getContent())
                .timeStamp(chatMessage.getTimeStamp())
                .build();
    }

}
