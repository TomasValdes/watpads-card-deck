package com.sordle.watpadsCardDeck.entity

import com.sordle.watpadsCardDeck.model.Cards
import jakarta.persistence.*
import java.io.Serializable

@Embeddable
data class PlayerId(
    val gameId: Long = 0,
    val userId: Long = 0
) : Serializable

/**
 * Entity that represents player in a game.
 * Each player has a single game and user they are associated with.
 * Primary key is (gameId, userId)
 */
@Entity
data class Player (
    @EmbeddedId
    val id: PlayerId = PlayerId(),

    @ManyToOne(optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    val user: User = User(),

    @ElementCollection
    val playerHand: MutableList<Cards> = mutableListOf(),

    // Defaults to rock, should be reassigned later
    val trumpCard: Cards = Cards.Rock
)