package com.example.chat_v1.controller;

import com.example.chat_v1.dto.chat.ChatMessageInputDTO;
import com.example.chat_v1.dto.chat.ChatMessageOutputDTO;
import com.example.chat_v1.dto.chatroom.ChatRoomOutputDTO;
import com.example.chat_v1.dto.user.UserProfileResponseDTO;
import com.example.chat_v1.service.ChatMessageService;
import com.example.chat_v1.service.ChatRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4300"})
@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    public ChatController(
            SimpMessagingTemplate simpMessagingTemplate,
            ChatMessageService chatMessageService,
            ChatRoomService chatRoomService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(
            @Payload ChatMessageInputDTO chatMessageInputDTO
    ){
        ChatMessageOutputDTO chatMessageOutputDTO = chatMessageService.save(chatMessageInputDTO);
        simpMessagingTemplate.convertAndSendToUser(
                chatMessageOutputDTO.getReceiverId().toString(),
                "/queue/messages",
                chatMessageOutputDTO
        ); // /user/{userId}/queue/messages
    }


    @GetMapping("/api/messages/{senderId}/{receiverId}")
    public ResponseEntity<List<ChatMessageOutputDTO>> getMessages(
            @PathVariable("senderId") Long senderId,
            @PathVariable("receiverId") Long receiverId
    ) {
        List<ChatMessageOutputDTO> chatMessages = chatMessageService.findChatMessages(senderId, receiverId);
        return new ResponseEntity<>(chatMessages, HttpStatus.OK);
    }

    @GetMapping("/api/messages/chatroom/{senderId}")
    public ResponseEntity<List<ChatRoomOutputDTO>> getUserChatrooms(@PathVariable("senderId") Long senderId){
        List<ChatRoomOutputDTO> chatrooms = chatRoomService.findChatroomsBySender(senderId);
        return new ResponseEntity<>(chatrooms, HttpStatus.OK);
    }

    @GetMapping("/api/messages/chatrooms")
    public ResponseEntity<List<UserProfileResponseDTO>> getChatroomsUsers(@RequestParam("chatroomId") List<Long> chatroomIds,
                                                                          @RequestParam("token") String token){
        List<UserProfileResponseDTO> users = chatRoomService.getUsersByChatrooms(chatroomIds, token);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
