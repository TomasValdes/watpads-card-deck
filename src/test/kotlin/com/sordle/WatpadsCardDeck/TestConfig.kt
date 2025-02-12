package com.sordle.watpadsCardDeck

import com.sordle.watpadsCardDeck.entity.Game
import com.sordle.watpadsCardDeck.entity.Lobby
import com.sordle.watpadsCardDeck.entity.Player
import com.sordle.watpadsCardDeck.entity.User
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession
import org.springframework.web.socket.WebSocketSession

class TestConfig {
    var testUser = User(
        userId = 213412,
        userName = "Test User"
    )

    var testUserTwo = User(
        userId = 345434,
        userName = "Test User Two"
    )

    var testLobby = Lobby(
        gameId = 1,
    )

    var testGame = Game(
        gameId = 1,
        playerOne = Player(user = testUser),
        playerTwo = Player(user = testUserTwo)
    )

    var testWebSocketLobbySession: WebSocketSession = StandardWebSocketSession(
        null,
        mapOf("gameId" to testLobby.gameId, "userId" to testUser.userId),
        null,
        null
    )

    var testWebSocketGameSession: WebSocketSession = StandardWebSocketSession(
        null,
        mapOf("gameId" to testGame.gameId, "userId" to testUser.userId),
        null,
        null
    )
}