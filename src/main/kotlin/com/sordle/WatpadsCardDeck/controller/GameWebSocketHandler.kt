package com.sordle.watpadsCardDeck.controller

import com.sordle.watpadsCardDeck.service.GameService
import com.sordle.watpadsCardDeck.service.MessageService
import com.sordle.watpadsCardDeck.service.UserService
import com.sordle.watpadsCardDeck.util.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.WebSocketSession

/**
 * Handles websocket that is used to play through game
 */
@Component
class GameWebSocketHandler(
    private val gameService: GameService,
    private val messageService: MessageService,
    private val gameSessionManager: SessionManager,
    private val userService: UserService
) : TextWebSocketHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handleTransportError(session: WebSocketSession, throwable: Throwable) {
        logger.info(
            "A transport error occurred for user with session id ${session.id}. Error Message: ${throwable.localizedMessage}")
        throw throwable
    }

    /**
     * Finds a game for given session then associates the session with the game
     */
    @Transactional
    override fun afterConnectionEstablished(session: WebSocketSession) {
        /**
         * Temporary work around until security is implemented
         * Currently, every session will create a new anonymous user
         */
        session.setUser(userService.createAnonymousUser())
        session.setGame(gameService.getGameToJoin())
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
        if (gameService.getGame(session.gameId) != null)
            messageService.handleMessageToGame(session, message.payload)
        else
            logger.info("Message from lobby ${session.gameId} ignored")
    }
}