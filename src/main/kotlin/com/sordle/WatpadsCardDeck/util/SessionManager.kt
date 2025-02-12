package com.sordle.watpadsCardDeck.util

import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

/**
 * Used to manage WebSocketSessions, grouping them by their game Ids
 */
@Component
class SessionManager {

    private val gameSessions: MutableMap<Long, MutableList<WebSocketSession>> = ConcurrentHashMap()

    fun addSession(gameId: Long, session: WebSocketSession) {
        gameSessions.computeIfAbsent(gameId) { mutableListOf() }.add(session)
    }

    fun removeSession(gameId: Long, session: WebSocketSession) {
        gameSessions[gameId]?.remove(session)
        if (gameSessions[gameId]?.isEmpty() == true) {
            gameSessions.remove(gameId)
        }
    }

    fun getSessions(gameId: Long): List<WebSocketSession>? {
        return gameSessions[gameId]
    }

    fun sendMessageToGame(gameId: Long, obj: Any){
        val sessions = getSessions(gameId)
        sessions?.forEach { s -> s.sendObjectMessage(obj)}
    }

    fun endGameSessions(gameId: Long){
        val sessions = getSessions(gameId)?.toList() ?: return
        sessions.forEach { s -> s.close(CloseStatus(1000))}
        gameSessions.remove(gameId)
    }

    fun sendMessageToPlayerInGame(gameId: Long, userId: Long, obj: Any){
        val sessions = getSessions(gameId)
        if (sessions != null) {
            sessions.find { s -> s.userId == userId }?.sendObjectMessage(obj)
        }
    }
}
