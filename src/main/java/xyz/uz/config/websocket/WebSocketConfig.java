package xyz.uz.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket 配置
 */

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 定义客户端的连接点
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new AuthHandshakeHandler())
                .withSockJS();
    }

    /**
     * 配置订阅前缀
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 定义广播主题前缀（后端向前端发送消息的统一前缀）
        registry.enableSimpleBroker("/topic","/queue");

        registry.setApplicationDestinationPrefixes("/user");

        // 定义前端向后端发送消息被处理的前缀，配置应接受 Controller 处理的统一前缀（没定义这个Controller）
        registry.setApplicationDestinationPrefixes("/app");
    }
}
