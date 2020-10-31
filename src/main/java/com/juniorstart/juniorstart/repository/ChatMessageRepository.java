package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.ChatMessage;
import com.juniorstart.juniorstart.model.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {

    long countBySenderIdAndRecipientIdAndStatus(
            String senderId, String recipientId, MessageStatus status);

    List<ChatMessage> findByChatId(String chatId);
}
