package com.sordle.watpadsCardDeck.model

import com.sordle.watpadsCardDeck.entity.Game
import com.sordle.watpadsCardDeck.entity.GameStates

data class GameResponse (
    val gameId: Long,
    val gameState: GameStates,
    val playerOneUserId: Long?,
    val playerTwoUserId: Long?
    ) {
    constructor(game: Game) : this(
        gameId = game.gameId,
        gameState = game.gameState,
        playerOneUserId = game.playerOne?.userId,
        playerTwoUserId = game.playerTwo?.userId
    )
}