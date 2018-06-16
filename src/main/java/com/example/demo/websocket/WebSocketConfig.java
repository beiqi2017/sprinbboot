package com.example.demo.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(),"/myHandler").addInterceptors(new WebSocketInterceptor()); //支持websocket 的访问链接
        registry.addHandler(myHandler(),"/sockjs/myHandler").addInterceptors(new WebSocketInterceptor()).withSockJS(); //不支持websocket的访问链接
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new MyHandler();
    }

}