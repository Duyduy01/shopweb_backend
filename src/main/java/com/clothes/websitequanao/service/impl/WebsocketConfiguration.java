package com.clothes.websitequanao.service.impl;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

import static com.clothes.websitequanao.common.Consts.IpAdress.IPADRESS;


@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfiguration implements WebSocketMessageBrokerConfigurer  {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect").setAllowedOriginPatterns(IPADRESS).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/ws");
        config.enableSimpleBroker("/topic");
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

