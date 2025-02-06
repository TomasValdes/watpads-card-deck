package com.sordle.watpadsCardDeck.config

import com.sordle.watpadsCardDeck.controller.GameWebSocketHandler

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor

@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val gameWebSocketHandler: GameWebSocketHandler
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(gameWebSocketHandler, "/web/game")
            // Interceptor to set initial session attributes, to be used for authorization later
            .addInterceptors(HttpSessionHandshakeInterceptor())
            .setAllowedOrigins("http://localhost:3000")
    }
}