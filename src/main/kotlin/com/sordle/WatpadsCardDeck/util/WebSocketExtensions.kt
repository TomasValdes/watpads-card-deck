package com.sordle.watpadsCardDeck.util

import org.slf4j.LoggerFactory
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import com.google.gson.Gson
import com.sordle.watpadsCardDeck.entity.GameSession

/**
 * Util for websockets
 */

private val logger = LoggerFactory.getLogger("com.sordle.watpadsCardDeck.controller.GameWebSocketHandler")

val WebSocketSession.game: GameSession
    get() = attributes["game"] as GameSession

val WebSocketSession.userId: Long
    get() = attributes["userId"] as Long

fun WebSocketSession.setGame(game: GameSession) {
    attributes["game"] = game
}

fun WebSocketSession.sendObjectMessage(obj: Any) {
    // Might be more efficient to have a project wide deserializer
    sendStringMessage(Gson().toJson(obj))
}

fun WebSocketSession.sendStringMessage(msg: String) {
    try {
        sendMessage(TextMessage(msg))
    } catch (e: Exception) {
        val shortenedMsg = if (msg.length > 40) msg.substring(0, 40) + "..." else msg
        logger.error("Failed to send message '{}' to {}: {}", shortenedMsg, id, e.toString())
    }
}