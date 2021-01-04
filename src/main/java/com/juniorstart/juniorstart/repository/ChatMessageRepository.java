package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.ChatMessage;
import com.juniorstart.juniorstart.model.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    long countBySenderIdAndRecipientIdAndStatus(
            String senderId, String recipientId, MessageStatus status);

    Optional<ChatMessage> findBySenderIdAndRecipientId(String senderId, String recipientId);

    @Modifying
    @Transactional
    @Query("update ChatMessage u set u.status = :status where u.recipientId = :recipientId and u.senderId = :senderId")
    void updateStatuses(@Param(value = "status") MessageStatus status, @Param(value = "recipientId") String recipientId, @Param(value = "senderId") String senderId);

    @Query("SELECT u FROM ChatMessage u WHERE u.chatId = :chatId ORDER BY timestamp ASC")
    List<ChatMessage> findByChatIdByDateAsc(@Param(value = "chatId") String chatId);

}
