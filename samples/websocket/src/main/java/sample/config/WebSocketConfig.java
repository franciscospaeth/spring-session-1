package sample.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.socket.server.SessionRepositoryMessageInterceptor;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import sample.data.ActiveWebSocketUserRepository;
import sample.security.SecurityContextSubProtocolEventHandler;
import sample.websocket.WebSocketConnectHandler;
import sample.websocket.WebSocketDisconnectHandler;

@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfig<S extends ExpiringSession> extends AbstractWebSocketMessageBrokerConfigurer implements WebSocketConfigurer {

    @Autowired
    SessionRepository<S> sessionRepository;

    @Bean
    public SecurityContextSubProtocolEventHandler<SessionConnectEvent> webSocketConnectHandler(SimpMessageSendingOperations messagingTemplate, ActiveWebSocketUserRepository repository) {
        WebSocketConnectHandler<S> delegate = new WebSocketConnectHandler<S>(messagingTemplate, repository);
        return new SecurityContextSubProtocolEventHandler<SessionConnectEvent>(delegate);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/messages")
                .withSockJS()
                .setInterceptors(sessionRepositoryInterceptor());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(sessionRepositoryInterceptor());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/queue/", "/topic/");
        registry.enableStompBrokerRelay("/queue/","/topic/");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Bean
    public SessionRepositoryMessageInterceptor<S> sessionRepositoryInterceptor() {
        return new SessionRepositoryMessageInterceptor<S>(sessionRepository);
    }

    @Bean
    public SecurityContextSubProtocolEventHandler<SessionDisconnectEvent> webSocketDisconnectHandler(SimpMessageSendingOperations messagingTemplate, ActiveWebSocketUserRepository repository) {
        WebSocketDisconnectHandler<S> delegate = new WebSocketDisconnectHandler<S>(messagingTemplate, repository);
        return new SecurityContextSubProtocolEventHandler<SessionDisconnectEvent>(delegate);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
            .addHandler(sessionRepositoryInterceptor(), "/messages");
    }
}