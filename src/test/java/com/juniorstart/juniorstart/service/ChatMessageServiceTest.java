package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.model.ChatMessage;
import com.juniorstart.juniorstart.model.MessageStatus;
import com.juniorstart.juniorstart.repository.ChatMessageRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceTest {

    @Mock
    ChatMessageRepository chatMessageRepository;

    @Mock
    ChatRoomService chatRoomService;

    @InjectMocks
    ChatMessageService chatMessageService;

    ChatMessage chatMessage;
    List<ChatMessage> chatMessages;
    ChatMessage delivedMessage;

    @BeforeEach
    public void initialize() throws Exception {
        chatMessage = ChatMessage.builder()
                .chatId("12_11")
                .content("Default Message")
                .id(12l)
                .recipientId("12")
                .senderId("11")
                .recipientName("RecipinientName")
                .senderName("SenderName")
                .status(MessageStatus.DELIVERED)
                .timestamp(new Date())
                .build();

        chatMessages = new ArrayList<>();
        chatMessages.add(chatMessage);
        delivedMessage = chatMessage;
        delivedMessage.setStatus(MessageStatus.DELIVERED);

    }

    @Test
    public void should_sendMessage() throws Exception {
        when(chatRoomService.getChatId(anyString(), anyString(), anyBoolean())).thenReturn(Optional.of(chatMessage.getChatId()));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        ChatMessage response = chatMessageService.sendMessage(chatMessage);

        assertTrue(response.getChatId().contains(chatMessage.getChatId()));
        assertTrue(response.getContent().contains(chatMessage.getContent()));
        assertTrue(response.getRecipientId().contains(chatMessage.getRecipientId()));
        verify(chatRoomService, times(1)).getChatId(anyString(), anyString(), anyBoolean());
        verify(chatMessageRepository, times(1)).save(any(ChatMessage.class));
    }

    @Test
    public void should_saveMessage() throws Exception {
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);
        ChatMessage response = chatMessageService.save(chatMessage);

        assertTrue(response.getChatId().contains(chatMessage.getChatId()));
        assertTrue(response.getStatus().equals(MessageStatus.RECEIVED));
        verify(chatMessageRepository, times(1)).save(any(ChatMessage.class));
    }

    @Test
    public void should_countNewMessages() throws Exception {
        when(chatMessageRepository.countBySenderIdAndRecipientIdAndStatus(anyString(), anyString(), any())).thenReturn(12l);

        Long response = chatMessageService.countNewMessages(chatMessage.getSenderId(), chatMessage.getRecipientId());

        assertEquals(response, 12l);
      verify(chatMessageRepository, times(1)).countBySenderIdAndRecipientIdAndStatus(anyString(), anyString(), any());
    }


    @Test
    public void should_findChatMessage() throws Exception {
        when(chatRoomService.getChatId(anyString(), anyString(), anyBoolean())).thenReturn(Optional.of(chatMessage.getChatId()));
        when(chatMessageRepository.findByChatIdByDateAsc(anyString())).thenReturn(chatMessages);
        List<ChatMessage> response = chatMessageService.findChatMessage(chatMessage.getSenderId(), chatMessage.getRecipientId());

        assertTrue(response.get(0).getChatId().contains(chatMessage.getChatId()));
        assertTrue(response.get(0).getContent().contains(chatMessage.getContent()));
        verify(chatRoomService, times(1)).getChatId(anyString(), anyString(), anyBoolean());
        verify(chatMessageRepository, times(1)).findByChatIdByDateAsc(anyString());
    }

    @Test
    public void should_findById() throws Exception {
        when(chatMessageRepository.findById(anyLong())).thenReturn(Optional.of(chatMessage));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(delivedMessage);

        ChatMessage response = chatMessageService.findById(chatMessage.getId());

        assertTrue(response.getContent().contains(chatMessage.getContent()));
        assertTrue(response.getStatus().equals(MessageStatus.DELIVERED));
        verify(chatMessageRepository, times(1)).findById(anyLong());
        verify(chatMessageRepository, times(1)).save(any(ChatMessage.class));
    }






}
