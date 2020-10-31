package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.model.ChatRoom;
import com.juniorstart.juniorstart.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/** Represents a chatRoom service
 * @author Grzegorz Szczęsny
 * @since 1.0
 * @version 1.0
 */
@Service
public class ChatRoomService {

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

     private final ChatRoomRepository chatRoomRepository;

    /**
     * Get a chat's room identification string.
     * @param senderId A sender identification string.
     * @param recipientId A recipientIn string
     * @param createIfNotExist the boolean that tells us if we should create new chat's room.
     * @return
     */
    public Optional<String> getChatId(
            String senderId, String recipientId, boolean createIfNotExist) {

        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(!createIfNotExist) {
                        return  Optional.empty();
                    }
                    var chatId =
                            String.format("%s_%s", senderId, recipientId);

                    ChatRoom senderRecipient = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .senderId(senderId)
                            .recipientId(recipientId)
                            .build();

                    ChatRoom recipientSender = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .senderId(recipientId)
                            .recipientId(senderId)
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }
}