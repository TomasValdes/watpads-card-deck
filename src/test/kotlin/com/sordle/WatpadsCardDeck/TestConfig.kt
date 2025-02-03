package com.sordle.watpadsCardDeck

import com.sordle.watpadsCardDeck.entity.Game
import com.sordle.watpadsCardDeck.entity.Player
import com.sordle.watpadsCardDeck.entity.User
import jakarta.websocket.server.HandshakeRequest
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.server.support.DefaultHandshakeHandler

object TestConfig {
    var testUser = User(
        userId = 213412,
        userName = "Test User"
    )

    var testUserTwo = User(
        userId = 345434,
        userName = "Test User Two"
    )

    var testGame = Game(
        gameId = 1,
        playerOne = Player(user = testUser),
        playerTwo = Player(user = testUserTwo)
    )

    var testWebSocketSession: WebSocketSession = StandardWebSocketSession(
        null,
        mapOf("gameId" to testGame.gameId, "userId" to testUser.userId),
        null,
        null
    )
}