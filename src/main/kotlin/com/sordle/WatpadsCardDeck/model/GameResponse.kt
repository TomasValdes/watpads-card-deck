package com.sordle.watpadsCardDeck.model

import com.sordle.watpadsCardDeck.entity.Game
import com.sordle.watpadsCardDeck.entity.GameStates

/**
 * Message returned by application to describe current game state
 */
data class GameResponse (
    val gameId: Long,
    val gameState: GameStates,
    val playerOneUserId: Long,
    val playerTwoUserId: Long,
    val playerOneMove: Cards?,
    val playerTwoMove: Cards?,
    val winner: Long?
    ) {
    constructor(game: Game) : this(
        gameId = game.gameId,
        gameState = game.gameState,
        playerOneUserId = game.playerOne.user.userId,
        playerTwoUserId = game.playerTwo.user.userId,
        playerOneMove = game.playerOne.move,
        playerTwoMove = game.playerTwo.move,
        winner = game.winner?.user?.userId
    )
}