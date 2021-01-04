package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.repository.ChatMessageRepository;
import com.juniorstart.juniorstart.repository.ChatRoomRepository;
import com.juniorstart.juniorstart.repository.UserDao;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.core.Is.is;

public class ChatControllerTest extends ControllerIntegrationTest  {



    @LocalServerPort
    int port;

    private String URL;

    User recipient;
    User sender;
    ChatMessage chatMessage;

    private static final String SEND_CREATE_MESSAGE_ENDPOINT = "/app/chat";
    private static final String SUBSCRIBE_CREATE_MESSAGE_ENDPOINT = "/queue/messages";


    private CompletableFuture<ChatNotification> completableFuture;

    WebSocketStompClient stompClient;
     ChatRoom chatRoom;
      ChatNotification chatNotification;
      String chatId;
      ChatMessage result;

    @Autowired
    UserDao userDao;

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @BeforeEach
    public void initialize() throws Exception {

        completableFuture = new CompletableFuture<>();

        sender = new User();
        sender.setName("SomeName");
        sender.setEmail("example@example.com");
        sender.setPassword("password");
        sender.setProvider(AuthProvider.local);

        recipient = new User();
        recipient.setName("someRecipientName");
        recipient.setEmail("recipient@example.com");
        recipient.setPassword("passwordRecipient");
        recipient.setProvider(AuthProvider.local);


        sender = userDao.save(sender);
        recipient = userDao.save(recipient);

        chatMessage = ChatMessage.builder()
                .timestamp(new Date()).status(MessageStatus.DELIVERED).senderName(sender.getName())
                .recipientName(recipient.getName())
                .senderId(sender.getPublicId().toString()).recipientId(recipient.getName()).build();

        chatId = String.format("%s_%s", sender.getPublicId(), recipient.getPublicId());
        chatRoom = ChatRoom.builder().chatId(chatId).recipientId(recipient.getPublicId().toString()).senderId(sender.getPublicId().toString()).build();
        chatRoomRepository.save(chatRoom);
        result = chatMessageRepository.save(chatMessage);
    }

    @AfterEach
    public void afterAll() {
        userDao.delete(sender);
        userDao.delete(recipient);
        chatMessageRepository.deleteAll();
        chatRoomRepository.deleteAll();
    }


    @Override
    @BeforeEach
    void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void should_receiveMessageFromTheServer() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
        stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());


        StompSession session = stompClient.connect("http://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {}).get(1, SECONDS);

        session.subscribe("/user/" + chatMessage.getRecipientId() + SUBSCRIBE_CREATE_MESSAGE_ENDPOINT, new CreateChatApplicationStompFrameHandler() {});

        session.send("/app/chat", chatMessage);

        ChatNotification response = completableFuture.get(5, SECONDS);

        assertNotNull(response);
        assertEquals(response.getSenderId(), chatMessage.getSenderId());
        assertEquals(response.getSenderName(), chatMessage.getSenderName());
    }


    @Test
    @WithUserDetails("someEmail")
    public void should_countNewMessage() throws Exception {
        mockMvc.perform(get("/messages/" + sender.getPublicId() + "/" + recipient.getPublicId() + "/count"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("someEmail")
    public void should_findChatMessages() throws Exception {
        mockMvc.perform(get("/messages/" + sender.getPublicId() + "/" + recipient.getPublicId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("someEmail")
    public void should_findMessage() throws Exception {
        mockMvc.perform(get("/messages/" + result.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId",is(result.getChatId())))
                .andExpect(jsonPath("$.senderId",is(result.getSenderId())))
                .andExpect(jsonPath("$.recipientId",is(result.getRecipientId())))
                .andExpect(jsonPath("$.senderName",is(result.getSenderName())))
                .andExpect(jsonPath("$.content",is(result.getContent())));
    }

    private class CreateChatApplicationStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return ChatNotification.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            completableFuture.complete((ChatNotification) o);
        }
    }


    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

}
