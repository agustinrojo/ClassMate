package com.example.chat_v1.service.mapper;

import com.example.chat_v1.dto.chat.ChatMessageInputDTO;
import com.example.chat_v1.dto.chat.ChatMessageOutputDTO;
import com.example.chat_v1.dto.file.FileDTO;
import com.example.chat_v1.entity.Attachment;
import com.example.chat_v1.entity.ChatMessage;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageMapper {

    public ChatMessage mapInputDTOToChatMessage(ChatMessageInputDTO chatMessageInputDTO){
        ChatMessage chatMessage = ChatMessage.builder()
                .senderId(chatMessageInputDTO.getSenderId())
                .receiverId(chatMessageInputDTO.getReceiverId())
                .content(chatMessageInputDTO.getContent())
                .build();
        if(chatMessageInputDTO.getAttachment() != null){
            chatMessage.setAttachment(mapFileDTOToAttachment(chatMessageInputDTO.getAttachment()));
        }
        return chatMessage;
    }

    public ChatMessageOutputDTO mapChatMessageToOutputDTO(ChatMessage chatMessage){
        ChatMessageOutputDTO chatMessageOutputDTO = ChatMessageOutputDTO.builder()
                .senderId(chatMessage.getSenderId())
                .receiverId(chatMessage.getReceiverId())
                .chatId(chatMessage.getChatId())
                .content(chatMessage.getContent())
                .timeStamp(chatMessage.getTimeStamp())
                .build();
        if(chatMessage.getAttachment() != null){
            chatMessageOutputDTO.setAttachment(mapAttachmentToFileDTO(chatMessage.getAttachment()));
        }
        return chatMessageOutputDTO;
    }

    public Attachment mapFileDTOToAttachment(FileDTO fileDTO){
        return Attachment.builder()
                .id(fileDTO.getId())
                .size(fileDTO.getSize())
                .originalFilename(fileDTO.getOriginalFilename())
                .contentType(fileDTO.getContentType())
                .build();
    }

    public FileDTO mapAttachmentToFileDTO(Attachment attachment){
        return FileDTO.builder()
                .id(attachment.getId())
                .size(attachment.getSize())
                .originalFilename(attachment.getOriginalFilename())
                .contentType(attachment.getContentType())
                .build();
    }

}
