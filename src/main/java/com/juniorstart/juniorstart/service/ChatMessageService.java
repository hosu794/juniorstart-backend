package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.ChatMessage;
import com.juniorstart.juniorstart.model.MessageStatus;
import com.juniorstart.juniorstart.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Long> countNewMessages(String senderId, String recipientId) {
        return ResponseEntity.ok(chatMessageRepository.countBySenderIdAndRecipientIdAndStatus(senderId, recipientId, MessageStatus.RECEIVED));
    }

    /**
     * Find a chat Messages by senderId and recipientId
     * @param senderId a sender identification number.
     * @param recipientId a recipient identification number.
     * @return a {@link List} that contains a messages, which belongs to sender and recipient.
     */
    public ResponseEntity<List<ChatMessage>> findChatMessage(String senderId, String recipientId) {

        var chatId = receiveChatRoomId(senderId, recipientId);

        var messages = chatId.map(cId -> chatMessageRepository.findByChatIdByDateAsc(cId)).orElse(new ArrayList<>());

        boolean isMessageNotEmpty = messages.size() > 0;

        if (isMessageNotEmpty) {
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
        }

        return ResponseEntity.ok(messages);
    }

    /**
     * Find a chatMessage by identification number.
     * @param id A identification number.
     * @return a found {@link ChatMessage}.
     */
    public ResponseEntity<ChatMessage> findById(Long id) {
        return chatMessageRepository.findById(id).map(chatMessage -> {
            ChatMessage result = setStatusAndSaveMessage(chatMessage);
            return ResponseEntity.ok(result);
        }).orElseThrow(() -> new ResourceNotFoundException("ChatMessage", "chatMessageId", id));
    }

    /**
     * Set status to DELIVER, save and return chat message
     * @param chatMessage A object contains message credentials
     * @return a saved {@link ChatMessage}
     */
    private ChatMessage setStatusAndSaveMessage(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.DELIVERED);
        return chatMessageRepository.save(chatMessage);
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

    /**
     * Receive a chat's room id from sender's id and recipient's id
     * @param senderId A sender identification number.
     * @param recipientId A recipient identification number.
     * @return If chat room exists a chat's room identification number.
     */
    private Optional<String> receiveChatRoomId(String senderId, String recipientId) {
        return chatRoomService.getChatId(senderId, recipientId, false);
    }

}
