package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.ChatRoom;
import com.juniorstart.juniorstart.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

/** Represents a chatRoom service
 * @author Grzegorz Szczęsny
 * @since 1.0
 * @version 1.0
 */
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;


    //I used  constructor, because without @Lazy annotation program doesn't work.
    public ChatRoomService(@Lazy ChatRoomRepository chatRoomRepository,@Lazy ChatMessageService chatMessageService) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageService = chatMessageService;
    }


    /**
     * Get a chat's room identification string.
     * @param senderId A sender identification string.
     * @param recipientId A recipientIn string
     * @param createIfNotExist the boolean that tells us if we should create new chat's room.
     * @return if exists a chat identification number.
     */
    public Optional<String> getChatId(String senderId,
                                      String recipientId,
                                      boolean createIfNotExist) {

        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {

                    if(!createIfNotExist) {
                        return  Optional.empty();
                    }
                    var chatId = formatChatId(senderId, recipientId);

                    ChatRoom senderRecipient = buildChatRoomForSenderRecipient(senderId, recipientId, chatId);

                    ChatRoom recipientSender = buildChatRoomForRecipientSender(senderId, recipientId, chatId);

                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);

                });
    }

    /**
     * Build a chatRoom object from given credentials for senderRecipient.
     * @param senderId A sender identification number.
     * @param recipientId A recipient identification number.
     * @param chatId A chat identification number.
     * @return A created {@link ChatRoom}
     */
    private ChatRoom buildChatRoomForSenderRecipient(String senderId, String recipientId, String chatId) {
        return ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();
    }

    /**
     * Build a chatRoom object from given credentials for recipientSender.
     * @param senderId A sender identification number.
     * @param recipientId A recipient identification number.
     * @param chatId A chat identification number.
     * @return a created {@link ChatRoom}
     */
    private ChatRoom buildChatRoomForRecipientSender(String senderId, String recipientId, String chatId) {
        return ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();
    }

    /**
     * Format chatId from senderId and recipientId
     * @param senderId A sender identification number.
     * @param recipientId A recipient identification number.
     * @return a created chatId, that was created from sender and recipient ids/
     */
    private String formatChatId(String senderId, String recipientId) {
        return String.format("%s_%s", senderId, recipientId);
    }
}