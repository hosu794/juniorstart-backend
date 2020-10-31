package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.ChatMessage;
import com.juniorstart.juniorstart.model.MessageStatus;
import com.juniorstart.juniorstart.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageService {


    public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatRoomService chatRoomService) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
    }

    private final ChatMessageRepository chatMessageRepository;

    private final ChatRoomService chatRoomService;


    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessage(String senderId, String recipientId) {
        return chatMessageRepository.countBySenderIdAndRecipientIdAndStatus(senderId, recipientId, MessageStatus.RECEIVED);
    }

    public List<ChatMessage> findChatMessage(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatId(senderId, recipientId, false);

        var messages = chatId.map(cId -> chatMessageRepository.findByChatId(cId)).orElse(new ArrayList<>());

        if (messages.size() > 0) {
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
        }

        return  messages;
    }

    public ChatMessage findById(String id) {
        return chatMessageRepository.findById(id).map(chatMessage -> {
            chatMessage.setStatus(MessageStatus.DELIVERED);
            return chatMessageRepository.save(chatMessage);
        }).orElseThrow(() -> new ResourceNotFoundException("ChatMessage", "chatMessageId", id));
    }


    private void updateStatuses(String senderId, String recipientId, MessageStatus status) {
        ChatMessage chatMessage = chatMessageRepository.findBySenderIdAndRecipientId(senderId, recipientId).orElseThrow(() -> new ResourceNotFoundException("ChatMessage", "senderId and recipientId", senderId + recipientId));
        chatMessage.setStatus(status);
        chatMessageRepository.save(chatMessage);
    }

}
