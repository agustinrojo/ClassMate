package com.example.chat_v1.service;

import com.example.chat_v1.dto.publisher.AddChatroomDTO;
import com.example.chat_v1.entity.ChatRoom;
import com.example.chat_v1.repository.ChatRoomRepository;
import com.example.chat_v1.service.publisher.ChatEventsPublisher;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(ChatRoomService.class);

    private final ChatEventsPublisher chatEventsPublisher;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, ChatEventsPublisher chatEventsPublisher)
    {
        this.chatRoomRepository = chatRoomRepository;
        this.chatEventsPublisher = chatEventsPublisher;
    }

    @Transactional
    public Optional<String> getChatRoomId(
            Long senderId,
            Long receiverId,
            boolean createNewRoomIfNotExists
    ) {
        LOGGER.info("llego a getChatRoomId()");
        return chatRoomRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(createNewRoomIfNotExists){
                        LOGGER.info("llego a .or()");
                        LOGGER.info(String.format("senderID %d", senderId));
                        LOGGER.info(String.format("receiverId %d", receiverId));
                        String chatId = createChatRoom(senderId, receiverId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    @Transactional
    private String createChatRoom(Long senderId, Long receiverId){
        LOGGER.info("llego a createChatRoom()");
        String chatId = String.format("%d_%d", senderId, receiverId);
        ChatRoom senderReceiver = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        ChatRoom receiverSender = ChatRoom.builder()
                .chatId(chatId)
                .senderId(receiverId)
                .receiverId(senderId)
                .build();
        chatRoomRepository.save(senderReceiver);
        chatRoomRepository.save(receiverSender);
        //agregar chatrooms a cada user mediante RabbitMQ
        chatEventsPublisher.publishAddChatroom(AddChatroomDTO.builder()
                .chatId(chatId)
                .userId(senderId)
                .build());
        chatEventsPublisher.publishAddChatroom(AddChatroomDTO.builder()
                .chatId(chatId)
                .userId(receiverId)
                .build());
        return chatId;
    }
}
