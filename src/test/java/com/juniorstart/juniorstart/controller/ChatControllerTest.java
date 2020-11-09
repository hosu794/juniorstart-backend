package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.*;
import lombok.Value;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ChatControllerTest extends ControllerIntegrationTest {


    private int port = 8080;
    private String URL;

    User recipient;
    User sender;
    ChatMessage chatMessage;

    private static final String SEND_CREATE_MESSAGE_ENDPOINT = "/app/chat";
    private static final String SUBSCRIBE_CREATE_MESSAGE_ENDPOINT = "/queue/messages";

    private CompletableFuture<ChatNotification> completableFuture;

    @BeforeEach
    public void initialize() {
        completableFuture = new CompletableFuture<>();
        URL =   URL = "ws://localhost:" + port;

        recipient = User.builder()
                .email("SomeEmail")
                .age(12)
                .emailVerified(false)
                .hiddenFromSearch(false)
                .imageUrl("imageUrl")
                .name("easyName")
                .password("easyPassword")
                .privateId(new UUID(12l, 12l))
                .provider(AuthProvider.local)
                .providerId("provider_id")
                .publicId(12l)
                .build();

        sender = User.builder()
                .email("SomeEmailSender")
                .age(12)
                .emailVerified(false)
                .hiddenFromSearch(false)
                .imageUrl("imageUrl")
                .name("easyName")
                .password("easyPassword")
                .privateId(new UUID(12l, 12l))
                .provider(AuthProvider.local)
                .providerId("provider_id")
                .publicId(13l)
                .build();

        chatMessage = ChatMessage.builder()
                .chatId("12_11")
                .content("Default Message")
                .senderId(sender.getPublicId().toString())
                .recipientId(recipient.getPublicId().toString())
                .senderId("11")
                .recipientName(recipient.getName())
                .senderName(sender.getName())
                .status(MessageStatus.DELIVERED)
                .build();
    }

    @Test
    public void should_processMessage() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe("/user/" + recipient.getPublicId() + SUBSCRIBE_CREATE_MESSAGE_ENDPOINT, new CreateGameStompFrameHandler());
        stompSession.send(SEND_CREATE_MESSAGE_ENDPOINT, chatMessage);
        ChatNotification notificationResponse = completableFuture.get(5, SECONDS);

        Assert.assertNotNull(notificationResponse);
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return  transports;
    }

    private class CreateGameStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            System.out.println(stompHeaders.toString());
            return ChatMessage.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            System.out.println((ChatNotification) o);
            completableFuture.complete((ChatNotification) o);
        }
    }

}
