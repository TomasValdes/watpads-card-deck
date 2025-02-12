package com.sordle.watpadsCardDeck.service

import com.sordle.watpadsCardDeck.TestConfig
import com.sordle.watpadsCardDeck.model.GameResponse
import com.sordle.watpadsCardDeck.repository.LobbyRepository
import com.sordle.watpadsCardDeck.repository.GameRepository
import com.sordle.watpadsCardDeck.repository.UserRepository
import com.sordle.watpadsCardDeck.util.SessionManager
import com.sordle.watpadsCardDeck.util.gameId
import io.mockk.*
import org.junit.jupiter.api.Test
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession
import kotlin.test.assertEquals

class GameServiceTest {
    private val lobbyRepository: LobbyRepository = mockk()
    private val gameRepository: GameRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val userService = UserService(userRepository)
    private val gameSessionManager: SessionManager = mockk()
    private val gameService = GameService(lobbyRepository, gameRepository,
        userService, gameSessionManager)

    private val testConfig = TestConfig()
    private val testWebSocketSessionTwo: WebSocketSession = StandardWebSocketSession(
        null,
        mapOf("gameId" to testConfig.testLobby.gameId, "userId" to testConfig.testUserTwo.userId),
        null,
        null
    )

    @Test
    fun `create new game in queue`() {
        every { lobbyRepository.findAllByOrderByCreatedDateAsc()} answers { emptyList() }
        every { lobbyRepository.save(any()) } answers {testConfig.testLobby}

        assertEquals(testConfig.testLobby, gameService.getGameToJoin())
        verify(exactly = 1) { lobbyRepository.save(any()) }
    }

    @Test
    fun `join game in queue as player 1`() {
        every { gameService.getLobby(testConfig.testWebSocketLobbySession.gameId) } answers {testConfig.testLobby}
        every { userService.getUser(testConfig.testUser.userId) } answers {testConfig.testUser}
        every { lobbyRepository.save(any()) } answers { testConfig.testLobby }
        gameService.joinGame(testConfig.testWebSocketLobbySession)

        assertEquals(testConfig.testUser, testConfig.testLobby.playerOne!!.user)
    }

    @Test
    fun `create new game`() {
        `join game in queue as player 1`()

        every { gameService.getLobby(testWebSocketSessionTwo.gameId) } answers {testConfig.testLobby}
        every { userService.getUser(testConfig.testUserTwo.userId) } answers {testConfig.testUserTwo}
        every { gameRepository.save(any()) } answers {testConfig.testGame}
        every { lobbyRepository.delete(testConfig.testLobby) } just runs
        every { gameSessionManager.sendMessageToGame(testConfig.testGame.gameId,
            GameResponse(testConfig.testGame)) } just runs

        gameService.joinGame(testWebSocketSessionTwo)

        verify(exactly = 1) { lobbyRepository.delete(testConfig.testLobby) }
        verify(exactly = 1) { gameRepository.save(any()) }
    }
}