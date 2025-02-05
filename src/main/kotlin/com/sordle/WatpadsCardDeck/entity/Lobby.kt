package com.sordle.watpadsCardDeck.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

/**
 * Temporary entity used to assign players to a game
 */
@Entity
data class Lobby (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    override val gameId: Long = 0,

    @OneToOne
    override var playerOne: Player? = null,

    @OneToOne
    override var playerTwo: Player? = null,

    @CreatedDate
    val createdDate: LocalDateTime = LocalDateTime.now()
) : GameSession