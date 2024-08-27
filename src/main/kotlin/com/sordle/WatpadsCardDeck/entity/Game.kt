package com.sordle.watpadsCardDeck.entity

import com.sordle.watpadsCardDeck.model.Cards
import jakarta.persistence.*

/**
 * Represents the states that the game could be in.
 * General game flow should go down the line as they appear in order
 */
enum class GameStates {
    // select the card you want to win with
    SelectingTrump,

    // select the cards you would like to add to the shared deck
    DraftingCards,

    // select the cards you would like to play each round
    PlayingCards,

    // reveal the blind to the winner who didn't win on win condition
    RevealingCards,

    // display the results of the game
    RoundResults
}

/**
 * Entity that tracks a game's state. Updated as game is played.
 */
@Entity
data class Game (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val gameId: Long = 0,

    val gameState: GameStates = GameStates.SelectingTrump,

    @ElementCollection
    val startingDeck: List<Cards> = listOf(),

    @ElementCollection
    val currentDeck: MutableList<Cards> = mutableListOf(),

    @OneToMany(mappedBy = "id.gameId", cascade = [CascadeType.ALL], orphanRemoval = true)
    val players: List<Player> = listOf()
    )