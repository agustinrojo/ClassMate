package com.example.chat_v1.service;

import com.example.chat_v1.client.IChatClient;
import com.example.chat_v1.dto.chatroom.ChatRoomOutputDTO;
import com.example.chat_v1.dto.publisher.AddChatroomDTO;
import com.example.chat_v1.dto.user.UserProfileSearchDTO;
import com.example.chat_v1.entity.ChatRoom;
import com.example.chat_v1.exception.ResourceNotFoundException;
import com.example.chat_v1.repository.ChatRoomRepository;
import com.example.chat_v1.service.mapper.ChatRoomMapper;
import com.example.chat_v1.service.publisher.ChatEventsPublisher;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomMapper chatRoomMapper;

    private final IChatClient chatClient;

    private final Logger LOGGER = LoggerFactory.getLogger(ChatRoomService.class);

    private final ChatEventsPublisher chatEventsPublisher;

    public ChatRoomService(
            ChatRoomRepository chatRoomRepository,
            ChatRoomMapper chatRoomMapper,
            ChatEventsPublisher chatEventsPublisher,
            IChatClient chatClient)
    {
        this.chatRoomRepository = chatRoomRepository;
        this.chatEventsPublisher = chatEventsPublisher;
        this.chatRoomMapper = chatRoomMapper;
        this.chatClient = chatClient;
    }

    public List<ChatRoomOutputDTO> findChatroomsBySender(Long senderId){
        List<ChatRoom> chatrooms = chatRoomRepository.findBySenderId(senderId);
        if(chatrooms.isEmpty()){
            return new ArrayList<>();
        }
        return chatrooms.stream()
                .map(chatRoomMapper::mapChatRoomToOutputDTO)
                .collect(Collectors.toList());
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
    public List<UserProfileSearchDTO> getUsersByChatrooms(List<Long> chatroomIds, String token){
        String bearerToken = "Bearer " + token;
        List<ChatRoom> chatRooms = chatRoomRepository.findByIdIn(chatroomIds);
        if (chatRooms.isEmpty()){
            LOGGER.info("No Chatrooms found");
            throw new ResourceNotFoundException("Chatroom", "id", "any");
        }
        List<Long> userIds = chatRooms.stream()
                .map(ChatRoom::getReceiverId)
                .toList();

        return chatClient.findMulitpleUsers(userIds, bearerToken).getBody();
    }

    @Transactional
    private String createChatRoom(Long senderId, Long receiverId){
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
        senderReceiver = chatRoomRepository.save(senderReceiver);
        receiverSender = chatRoomRepository.save(receiverSender);
        //agregar chatrooms a cada user mediante RabbitMQ
        chatEventsPublisher.publishAddChatroom(AddChatroomDTO.builder()
                .chatroomId(senderReceiver.getId())
                .userId(senderId)
                .build());
        chatEventsPublisher.publishAddChatroom(AddChatroomDTO.builder()
                .chatroomId(receiverSender.getId())
                .userId(receiverId)
                .build());
        return chatId;
    }
}
