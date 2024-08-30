package com.sordle.watpadsCardDeck.entity

import com.sordle.watpadsCardDeck.model.Cards
import jakarta.persistence.*

/**
 * Entity that represents player in a game.
 * Each player has a single game and user they are associated with.
 */
@Entity
data class Player (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val playerId: Long = 0,

    @ManyToOne
    val user: User = User(),

    @ElementCollection
    val hand: MutableList<Cards> = mutableListOf(),

    @ElementCollection
    var cardsAddedToDeck: MutableList<Cards> = mutableListOf(),

    var trumpCard: Cards? = null,

    var move: Cards? = null
)