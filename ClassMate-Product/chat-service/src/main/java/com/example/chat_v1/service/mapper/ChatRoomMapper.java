package com.example.chat_v1.service.mapper;

import com.example.chat_v1.dto.chatroom.ChatRoomOutputDTO;
import com.example.chat_v1.entity.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {

    @Mapping(source = "chatId", target = "chatId")
    @Mapping(source = "senderId", target = "senderId")
    @Mapping(source = "receiverId", target = "receiverId")
    ChatRoomOutputDTO mapChatRoomToOutputDTO(ChatRoom chatRoom);
}
