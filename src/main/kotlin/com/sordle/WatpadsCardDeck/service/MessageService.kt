package com.sordle.watpadsCardDeck.service

import com.sordle.watpadsCardDeck.entity.GameStates
import com.sordle.watpadsCardDeck.util.gameId
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession

@Service
class MessageService(
    private val gameService: GameService
) {
    fun handleMessage(session: WebSocketSession, messageString: String){
        val game = gameService.getGame(session.gameId)
        when(game.gameState){
            GameStates.SelectingTrump -> gameService.setTrump()
            GameStates.DraftingCards -> gameService.addCardsToDeck()
            GameStates.PlayingCards -> gameService.playCard()
            else -> {

            }
        }
    }
}