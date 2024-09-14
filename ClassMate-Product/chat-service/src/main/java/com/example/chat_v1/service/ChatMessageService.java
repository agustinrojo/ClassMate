package com.example.chat_v1.service;

import com.example.chat_v1.dto.chat.ChatMessageInputDTO;
import com.example.chat_v1.dto.chat.ChatMessageOutputDTO;
import com.example.chat_v1.dto.message.MessageNotificationEventDTO;
import com.example.chat_v1.entity.Attachment;
import com.example.chat_v1.entity.ChatMessage;
import com.example.chat_v1.publisher.ChatPublisher;
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
    private final ChatPublisher chatPublisher;

    public ChatMessageService(ChatMessageRepository chatMessageRepository,
                              ChatRoomService chatRoomService,
                              ChatMessageMapper chatMessageMapper,
                              AttachmentRepository attachmentRepository, ChatPublisher chatPublisher) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
        this.chatMessageMapper = chatMessageMapper;
        this.attachmentRepository = attachmentRepository;
        this.chatPublisher = chatPublisher;
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

        // Publish notification event
        MessageNotificationEventDTO eventDTO = new MessageNotificationEventDTO(newChatMessage.getReceiverId(), newChatMessage.getSenderId());
        chatPublisher.publishMessageNotificationEvent(eventDTO);
        
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
