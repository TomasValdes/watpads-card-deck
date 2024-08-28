package com.sordle.watpadsCardDeck.model

/**
 * Message sent in the play card phase to play provided card
 */
data class PlayCardRequest (
    val card : Cards
)