package com.sordle.watpadsCardDeck.entity

import com.sordle.watpadsCardDeck.model.Cards
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

/**
 * Represents the states that the game could be in.
 * General game flow should go down the line as they appear in order
 */
enum class GameStates {
    // status to signify game isn't ready to start
    WaitingForPlayers,

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

    var gameState: GameStates = GameStates.WaitingForPlayers,

    @ElementCollection
    val startingDeck: List<Cards> = listOf(),

    @ElementCollection
    var currentDeck: MutableList<Cards> = mutableListOf(),

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "user.userId", column = Column(name = "player_one_user_id")),
        AttributeOverride(name = "trumpCard", column = Column(name = "player_one_trump_card")),
        AttributeOverride(name = "playerHand", column = Column(name = "player_one_hand"))

    )
    var playerOne: Player? = null,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "user.userId", column = Column(name = "player_two_user_id")),
        AttributeOverride(name = "trumpCard", column = Column(name = "player_two_trump_card")),
        AttributeOverride(name = "playerHand", column = Column(name = "player_two_hand"))
    )
    var playerTwo: Player? = null,

    @CreatedDate
    val createdDate: LocalDateTime = LocalDateTime.now()
    )