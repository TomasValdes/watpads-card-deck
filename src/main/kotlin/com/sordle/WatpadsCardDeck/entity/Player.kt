package com.sordle.watpadsCardDeck.entity

import com.sordle.watpadsCardDeck.model.Cards
import jakarta.persistence.*
import java.io.Serializable

/**
 * Entity that represents player in a game.
 * Each player has a single game and user they are associated with.
 */
@Embeddable
data class Player (
    val userId: Long = 0,

    @ElementCollection
    val playerHand: MutableList<Cards> = mutableListOf(),

    val trumpCard: Cards? = null
)