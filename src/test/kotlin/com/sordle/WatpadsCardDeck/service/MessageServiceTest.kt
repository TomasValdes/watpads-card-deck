package com.sordle.watpadsCardDeck.service

import com.sordle.watpadsCardDeck.TestConfig
import com.sordle.watpadsCardDeck.entity.GameStates
import com.sordle.watpadsCardDeck.util.setGame
import io.mockk.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class MessageServiceTest {
    private val gameService: GameService = mockk()
    private val messageService = MessageService(gameService)

    private val testConfig = TestConfig()
    private val selectingTrumpTestGame = testConfig.testGame.copy(gameState = GameStates.SelectingTrump)
    private val selectTrumpTestMessage = "{ \"card\" : \"Rock\" }"
    private val draftingCardsTestGame = testConfig.testGame.copy(gameState = GameStates.DraftingCards)
    private val draftCardsTestMessage = "{ \"card\" : [\"Rock\", \"Scissors\"] }"
    private val playingCardsTestGame = testConfig.testGame.copy(gameState = GameStates.PlayingCards)


    @Test
    fun `handle set trump message`() {
        testConfig.testWebSocketGameSession.setGame(selectingTrumpTestGame)
        every { gameService.setTrump(testConfig.testWebSocketGameSession, any()) } just runs

        messageService.handleMessageToGame(testConfig.testWebSocketGameSession, selectTrumpTestMessage)

        verify(exactly = 1) { gameService.setTrump(testConfig.testWebSocketGameSession, any()) }
    }

    @Test
    fun `handle add card message`() {
        testConfig.testWebSocketGameSession.setGame(draftingCardsTestGame)
        every { gameService.addCardsToDeck(testConfig.testWebSocketGameSession, any()) } just runs

        messageService.handleMessageToGame(testConfig.testWebSocketGameSession, draftCardsTestMessage)

        verify(exactly = 1) { gameService.addCardsToDeck(testConfig.testWebSocketGameSession, any()) }
    }

    @Test
    fun `handle play card message`() {
        testConfig.testWebSocketGameSession.setGame(playingCardsTestGame)
        every { gameService.playCard(testConfig.testWebSocketGameSession, any()) } just runs

        messageService.handleMessageToGame(testConfig.testWebSocketGameSession, selectTrumpTestMessage)

        verify(exactly = 1) { gameService.playCard(testConfig.testWebSocketGameSession, any()) }
    }

    @Test
    fun `handle unexpected message`() {
        val exception = assertThrows<UnsupportedOperationException> {
            messageService.handleMessageToGame(testConfig.testWebSocketGameSession, selectTrumpTestMessage)
        }

        assertEquals(exception.message, "Unexpected message received")
    }

    @Test
    fun `handle incorrectly formatted message`() {
        val exception = assertThrows<UnsupportedOperationException> {
            messageService.handleMessageToGame(testConfig.testWebSocketGameSession, "{}")
        }

        assertEquals(exception.message, "Unexpected message received")
    }
}