package com.sordle.watpadsCardDeck.service

import com.sordle.watpadsCardDeck.TestConfig
import com.sordle.watpadsCardDeck.entity.Game
import com.sordle.watpadsCardDeck.entity.GameStates
import com.sordle.watpadsCardDeck.model.UserRequest
import com.sordle.watpadsCardDeck.model.toEntity
import com.sordle.watpadsCardDeck.repository.GameQueueRepository
import com.sordle.watpadsCardDeck.repository.GameRepository
import com.sordle.watpadsCardDeck.repository.UserRepository
import com.sordle.watpadsCardDeck.util.GameSessionManager
import com.sordle.watpadsCardDeck.util.gameId
import io.mockk.*
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals

class MessageServiceTest {
    private val gameService: GameService = mockk()
    private val messageService = MessageService(gameService)

    private val selectingTrumpTestGame = TestConfig.testGame.copy(gameState = GameStates.SelectingTrump)
    private val selectTrumpTestMessage = "{ \"card\" : \"Rock\" }"
    private val draftingCardsTestGame = TestConfig.testGame.copy(gameState = GameStates.DraftingCards)
    private val draftCardsTestMessage = "{ \"card\" : [\"Rock\", \"Scissors\"] }"
    private val playingCardsTestGame = TestConfig.testGame.copy(gameState = GameStates.PlayingCards)


    @Test
    fun `handle set trump message`() {
        every { gameService.getGame(TestConfig.testWebSocketSession.gameId) } answers { selectingTrumpTestGame }
        every { gameService.setTrump(TestConfig.testWebSocketSession, any()) } just runs

        messageService.handleMessage(TestConfig.testWebSocketSession, selectTrumpTestMessage)

        verify(exactly = 1) { gameService.setTrump(TestConfig.testWebSocketSession, any()) }
    }

    @Test
    fun `handle add card message`() {
        every { gameService.getGame(TestConfig.testWebSocketSession.gameId) } answers { draftingCardsTestGame }
        every { gameService.addCardsToDeck(TestConfig.testWebSocketSession, any()) } just runs

        messageService.handleMessage(TestConfig.testWebSocketSession, draftCardsTestMessage)

        verify(exactly = 1) { gameService.addCardsToDeck(TestConfig.testWebSocketSession, any()) }
    }

    @Test
    fun `handle play card message`() {
        every { gameService.getGame(TestConfig.testWebSocketSession.gameId) } answers { playingCardsTestGame }
        every { gameService.playCard(TestConfig.testWebSocketSession, any()) } just runs

        messageService.handleMessage(TestConfig.testWebSocketSession, selectTrumpTestMessage)

        verify(exactly = 1) { gameService.playCard(TestConfig.testWebSocketSession, any()) }
    }
}