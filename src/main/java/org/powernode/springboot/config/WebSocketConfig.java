package org.powernode.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.powernode.springboot.websocket.handler.ManagerWebSocketHandler;
import org.powernode.springboot.websocket.interceptor.AuthHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
    @Autowired
    private ManagerWebSocketHandler managerWebSocketHandler;
    @Autowired
    private AuthHandshakeInterceptor authHandshakeInterceptor;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(managerWebSocketHandler,"/manager/connect")
                .setAllowedOrigins("https://whswlibrarysystem.top","http://localhost:8081")
                //添加拦截器
                .addInterceptors(authHandshakeInterceptor)
                ;
    }
}
