package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.ChatMessage;
import com.juniorstart.juniorstart.model.ChatNotification;
import com.juniorstart.juniorstart.service.ChatMessageService;
import com.juniorstart.juniorstart.service.ChatRoomService;
import com.sun.mail.iap.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChatController {

    public ChatController(SimpMessagingTemplate simpMessagingTemplate, ChatMessageService chatMessageService, ChatRoomService chatRoomService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
    }

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        var chatId = chatRoomService.getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.get());

        ChatMessage saved = chatMessageService.save(chatMessage);

        simpMessagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),"/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSenderName()));
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessage(@PathVariable String senderId, @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.countNewMessage(senderId, recipientId));
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<?> findChatMessages(@PathVariable String senderId, @PathVariable String recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessage(senderId, recipientId));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> findMessage(@PathVariable String id) {
        return ResponseEntity.ok(chatMessageService.findById(id));
    }



}
