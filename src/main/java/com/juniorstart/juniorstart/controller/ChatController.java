package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.ChatMessage;
import com.juniorstart.juniorstart.model.ChatNotification;
import com.juniorstart.juniorstart.service.ChatMessageService;
import com.juniorstart.juniorstart.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class ChatController {


    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat")
    public void processMessage(@RequestBody ChatMessage chatMessage) {

        ChatMessage saved = chatMessageService.sendMessage(chatMessage);

        simpMessagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),"/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSenderName()));
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessage(@PathVariable String senderId, @PathVariable String recipientId) {
        return chatMessageService.countNewMessages(senderId, recipientId);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<?> findChatMessages(@PathVariable String senderId, @PathVariable String recipientId) {
        return chatMessageService.findChatMessage(senderId, recipientId);
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> findMessage(@PathVariable Long id) {
        return chatMessageService.findById(id);
    }

}
