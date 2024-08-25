package com.example.chat_v1.service;

import com.example.chat_v1.dto.chat.ChatMessageInputDTO;
import com.example.chat_v1.dto.chat.ChatMessageOutputDTO;
import com.example.chat_v1.entity.Attachment;
import com.example.chat_v1.entity.ChatMessage;
import com.example.chat_v1.repository.AttachmentRepository;
import com.example.chat_v1.repository.ChatMessageRepository;
import com.example.chat_v1.service.mapper.ChatMessageMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final ChatMessageMapper chatMessageMapper;
    private final AttachmentRepository attachmentRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository,
                              ChatRoomService chatRoomService,
                              ChatMessageMapper chatMessageMapper,
                              AttachmentRepository attachmentRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
        this.chatMessageMapper = chatMessageMapper;
        this.attachmentRepository = attachmentRepository;
    }

    @Transactional
    public ChatMessageOutputDTO save(ChatMessageInputDTO chatMessage){
        ChatMessage newChatMessage = chatMessageMapper.mapInputDTOToChatMessage(chatMessage);
        String chatId = chatRoomService.getChatRoomId(
                chatMessage.getSenderId(),
                chatMessage.getReceiverId(),
                true
        ).orElseThrow();

        newChatMessage.setChatId(chatId);
        newChatMessage.setTimeStamp(new Date());
        Attachment attachment = newChatMessage.getAttachment();
        if(attachment != null){
            attachmentRepository.save(attachment);
        }
        return chatMessageMapper.mapChatMessageToOutputDTO(chatMessageRepository.save(newChatMessage));
    }

    public List<ChatMessageOutputDTO> findChatMessages(
            Long senderId,
            Long receiverId
    ) {
        List<ChatMessage> chatMessages = chatRoomService.getChatRoomId(senderId, receiverId, false)
                .map(chatMessageRepository::findByChatId)
                .orElse(new ArrayList<>());

        return chatMessages.stream()
                .map(chatMessageMapper::mapChatMessageToOutputDTO)
                .collect(Collectors.toList());
    }
}
