package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.exception.HttpHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;


@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfiguration implements WebSocketMessageBrokerConfigurer  {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/portfolio").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/topic", "/queue");
    }

//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(myHandler(), "/connect")
//                .withSockJS();
//    }
//
//    @Bean
//    public WebSocketHandler myHandler() {
//        return new MyHandler();
//    }
//    //Where is listening to messages
//    public static final String SOCKET_RECEIVE_PREFIX = "/ws";
//
//    //Where messages will be sent.
//    public static final String SOCKET_SEND_PREFIX = "/topic/messages";
//
//    //URL where the client must subscribe.
//    public static final String SOCKETS_ROOT_URL = "/connect";

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker(SOCKET_SEND_PREFIX);
//        registry.setApplicationDestinationPrefixes(SOCKET_RECEIVE_PREFIX);
//    }

//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint(SOCKETS_ROOT_URL).addInterceptors(new HttpHandshakeInterceptor()).withSockJS();
//        registry.addEndpoint(SOCKETS_ROOT_URL).setAllowedOrigins("*");
//    }
}

