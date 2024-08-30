package com.sordle.watpadsCardDeck.model

/**
 * Used to send private information to player through websocket
 */
data class PlayerResponse (
    val hand: MutableList<Cards> = mutableListOf()
)