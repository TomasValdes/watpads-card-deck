package com.sordle.watpadsCardDeck.controller

import com.sordle.watpadsCardDeck.service.GameService
import com.sordle.watpadsCardDeck.service.MessageService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.WebSocketSession

/**
 * Handles websocket that is used to play through game
 */
@Component
class GameWebSocketHandler (
    private val gameService: GameService,
    private val messageService: MessageService
) : TextWebSocketHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handleTransportError(session: WebSocketSession, throwable: Throwable) {
        logger.info(
            "A transport error occurred for user with session id ${session.id}. Error Message: ${throwable.localizedMessage}")
        throw throwable
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        gameService.joinGame(session)
        logger.info("User with session id ${session.id} connected")
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info("Received message ${message.payload}")
        messageService.handleMessage(session, message.toString())
    }
}