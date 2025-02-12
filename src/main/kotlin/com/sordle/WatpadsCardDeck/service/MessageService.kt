package com.sordle.watpadsCardDeck.service

import com.sordle.watpadsCardDeck.entity.GameStates
import com.sordle.watpadsCardDeck.model.AddCardsRequest
import com.sordle.watpadsCardDeck.model.PlayCardRequest
import com.sordle.watpadsCardDeck.util.Conversion
import com.sordle.watpadsCardDeck.util.gameId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.socket.WebSocketSession

/**
 * Handles incoming websocket messages
 */
@Service
class MessageService(
    private val gameService: GameService
) {
    fun handleMessageToGame(session: WebSocketSession, messageString: String){
        val messageJson = Conversion.textToJson(messageString)
        // null check already done in GameWebSocketHandler.handleTextMessage
        val game = gameService.getGame(session.gameId)!!
        when (game.gameState) {
            GameStates.SelectingTrump -> gameService.setTrump(session, PlayCardRequest(messageJson))
            GameStates.DraftingCards -> gameService.addCardsToDeck(session, AddCardsRequest(messageJson))
            GameStates.PlayingCards -> gameService.playCard(session, PlayCardRequest(messageJson))
            else -> {
                throw UnsupportedOperationException("Unexpected message received")
            }
        }
    }
}