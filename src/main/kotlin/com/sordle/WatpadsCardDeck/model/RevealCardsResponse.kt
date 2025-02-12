package com.sordle.watpadsCardDeck.model

/**
 * Message sent when a player wins a round, reveals top cards of deck
 *
 * Syntax: {"revealedCards" : [[Cards], [Cards]]}
 */
data class RevealCardsResponse (
    val revealedCards : List<Cards>
)