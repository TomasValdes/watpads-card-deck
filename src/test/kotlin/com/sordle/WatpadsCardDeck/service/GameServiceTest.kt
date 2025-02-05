package com.sordle.watpadsCardDeck.service

import com.sordle.watpadsCardDeck.TestConfig
import com.sordle.watpadsCardDeck.entity.Lobby
import com.sordle.watpadsCardDeck.model.GameResponse
import com.sordle.watpadsCardDeck.repository.GameQueueRepository
import com.sordle.watpadsCardDeck.repository.GameRepository
import com.sordle.watpadsCardDeck.repository.UserRepository
import com.sordle.watpadsCardDeck.util.GameSessionManager
import io.mockk.*
import org.junit.jupiter.api.Test
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession
import kotlin.test.assertEquals

class GameServiceTest {
    private val gameQueueRepository: GameQueueRepository = mockk()
    private val gameRepository: GameRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val userService = UserService(userRepository)
    private val gameSessionManager: GameSessionManager = mockk()
    private val gameService = GameService(gameQueueRepository, gameRepository,
        userService, gameSessionManager)

    private val testWebSocketSessionTwo: WebSocketSession = StandardWebSocketSession(
        null,
        mapOf("gameId" to TestConfig.testGame.gameId, "userId" to TestConfig.testUserTwo.userId),
        null,
        null
    )

    private val testQueuedGame = Lobby(
        gameId = 1
    )

    @Test
    fun `create new game in queue`() {
        every { gameQueueRepository.findAllByOrderByCreatedDateAsc()} answers { emptyList() }
        every { gameQueueRepository.save(any()) } answers { testQueuedGame }

        assertEquals(testQueuedGame.gameId, gameService.getGameToJoin())
        verify(exactly = 1) { gameQueueRepository.save(any()) }
    }

    @Test
    fun `join game in queue as player 1`() {
        `create new game in queue`()

        every { gameService.getQueuedGame(1) } answers {testQueuedGame}
        every { userService.getUser(TestConfig.testUser.userId) } answers {TestConfig.testUser}
        gameService.joinGame(TestConfig.testWebSocketSession)

        assertEquals(TestConfig.testUser, testQueuedGame.playerOne!!.user)
    }

    @Test
    fun `create new game`() {
        `join game in queue as player 1`()

        every { gameService.getQueuedGame(1) } answers {testQueuedGame}
        every { userService.getUser(TestConfig.testUserTwo.userId) } answers {TestConfig.testUserTwo}
        every { gameRepository.save(any()) } answers {TestConfig.testGame}
        every { gameQueueRepository.delete(testQueuedGame) } just runs
        every { gameSessionManager.sendMessageToGame(TestConfig.testGame.gameId,
            GameResponse(TestConfig.testGame)) } just runs

        gameService.joinGame(testWebSocketSessionTwo)

        verify(exactly = 1) { gameQueueRepository.delete(testQueuedGame) }
        verify(exactly = 1) { gameRepository.save(any()) }
    }
}