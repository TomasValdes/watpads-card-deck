package com.sordle.watpadsCardDeck.controller

import com.sordle.watpadsCardDeck.service.GameService
import com.sordle.watpadsCardDeck.service.MessageService
import com.sordle.watpadsCardDeck.util.GameSessionManager
import com.sordle.watpadsCardDeck.util.gameId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.WebSocketSession

/**
 * Handles websocket that is used to play through game
 */
@Component
class GameWebSocketHandler (
    private val gameService: GameService,
    private val messageService: MessageService,
    private val gameSessionManager: GameSessionManager
) : TextWebSocketHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handleTransportError(session: WebSocketSession, throwable: Throwable) {
        logger.info(
            "A transport error occurred for user with session id ${session.id}. Error Message: ${throwable.localizedMessage}")
        throw throwable
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        gameSessionManager.addSession(session.gameId, session)
        gameService.joinGame(session)
        logger.info("User with session id ${session.id} connected")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        gameSessionManager.removeSession(session.gameId, session)
        logger.info("User with session id ${session.id} disconnected")
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info("Received message ${message.payload}")
        messageService.handleMessage(session, message.toString())
    }
}