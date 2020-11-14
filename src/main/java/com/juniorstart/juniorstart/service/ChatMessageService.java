package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.ChatMessage;
import com.juniorstart.juniorstart.model.MessageStatus;
import com.juniorstart.juniorstart.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

/** Represents a chat service
 * @author Grzegorz Szczęsny
 * @since 1.0
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    public ChatMessage sendMessage(ChatMessage chatMessage) {

        String chatId = chatRoomService.getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow(() -> new BadRequestException("Cannot find a chatId"));

        chatMessage.setChatId(chatId);

        return save(chatMessage);
    }

    /**
     * Create a new message.
     * @param chatMessage The message, that we send.
     * @return a created message.
     */
    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    /**
     * Count a messages, which were send and received by recipient.
     * @param senderId A sender identification number.
     * @param recipientId A recipient identification number.
     * @return A amount of received messages.
     */
    public long countNewMessages(String senderId, String recipientId) {
        return chatMessageRepository.countBySenderIdAndRecipientIdAndStatus(senderId, recipientId, MessageStatus.RECEIVED);
    }

    /**
     * Find a chat Message by senderId and recipientId
     * @param senderId a sender identification number.
     * @param recipientId a recipient identification number.
     * @return a {@link List} that contains a messages, which belongs to sender and recipient.
     */
    public List<ChatMessage> findChatMessage(String senderId, String recipientId) {

        var chatId = chatRoomService.getChatId(senderId, recipientId, false);

        var messages = chatId.map(cId -> chatMessageRepository.findByChatIdByDateAsc(cId)).orElse(new ArrayList<>());

        if (messages.size() > 0) {
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
        }

        return  messages;
    }

    /**
     * Find a chatMessage by identification number.
     * @param id A identification number.
     * @return a found {@link ChatMessage}.
     */
    public ChatMessage findById(Long id) {
        return chatMessageRepository.findById(id).map(chatMessage -> {
            chatMessage.setStatus(MessageStatus.DELIVERED);
            return chatMessageRepository.save(chatMessage);
        }).orElseThrow(() -> new ResourceNotFoundException("ChatMessage", "chatMessageId", id));
    }

    /**
     * Update statuses if message's list has at least one item.
     * @param senderId A sender identification number.
     * @param recipientId A recipient identification number.
     * @param status
     */
    private void updateStatuses(String senderId, String recipientId, MessageStatus status) {
        chatMessageRepository.updateStatuses(status, recipientId, senderId);
    }

}