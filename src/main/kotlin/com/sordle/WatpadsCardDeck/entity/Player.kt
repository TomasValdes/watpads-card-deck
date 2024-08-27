package com.sordle.watpadsCardDeck.entity

import com.sordle.watpadsCardDeck.model.Cards
import jakarta.persistence.*
import java.io.Serializable

/**
 * Entity that represents player in a game.
 * Each player has a single game and user they are associated with.
 * Primary key is (gameId, userId)
 */
@Embeddable
data class Player (
    @ManyToOne(optional = false, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", insertable=false, updatable=false)
    val user: User = User(),

    @ElementCollection
    val playerHand: MutableList<Cards> = mutableListOf(),

    // Defaults to rock, should be reassigned later
    val trumpCard: Cards = Cards.Rock
)