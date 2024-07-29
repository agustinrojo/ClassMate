package com.example.chat_v1.controller;

import com.example.chat_v1.dto.chat.ChatMessageInputDTO;
import com.example.chat_v1.dto.chat.ChatMessageOutputDTO;
import com.example.chat_v1.service.ChatMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate, ChatMessageService chatMessageService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatMessageService = chatMessageService;
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
}
