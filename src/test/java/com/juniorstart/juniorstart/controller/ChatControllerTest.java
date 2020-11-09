package com.juniorstart.juniorstart.controller;

import com.juniorstart.juniorstart.model.*;
import com.juniorstart.juniorstart.repository.UserDao;
import lombok.Value;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
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
import springfox.documentation.spring.web.json.Json;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;


public class ChatControllerTest extends ControllerIntegrationTest  {



    @LocalServerPort
    int port;

    private String URL;

    User recipient;
    User sender;
    ChatMessage chatMessage;

    private static final String SEND_CREATE_MESSAGE_ENDPOINT = "/app/chat";
    private static final String SUBSCRIBE_CREATE_MESSAGE_ENDPOINT = "/queue/messages";


     BlockingQueue<String> blockingQueue;
     WebSocketStompClient stompClient;
     ChatRoom chatRoom;
      ChatNotification chatNotification;
      String chatId;

    @Autowired
    UserDao userDao;

    @BeforeEach
    public void initialize() throws Exception {
        blockingQueue = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(new SockJsClient(
                asList(new WebSocketTransport(new StandardWebSocketClient()))
        ));

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

    }

    @Override
    void setUp() throws Exception {
        super.setUp();


    }

    @Test
    public void should_receiveMessageFromTheServer() throws Exception {
        System.out.println("Something");

        StompSession session = stompClient.connect("http://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {}).get(1, SECONDS);

        session.subscribe("/user/" + recipient.getPublicId() + SUBSCRIBE_CREATE_MESSAGE_ENDPOINT, new DefaultStompFrameHandler() {});

         chatNotification = new ChatNotification(ArgumentMatchers.anyLong(), recipient.getPublicId().toString(), recipient.getName());

         session.send("/app/chat", );
         Assert.assertEquals(chatNotification, blockingQueue.poll(1, SECONDS));
    }

    class DefaultStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return ChatNotification.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            blockingQueue.offer(new String((byte[]) o));
        }
    }

}
