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
    val gameId: Long = 0,

    @OneToOne
    var playerOne: Player? = null,

    @OneToOne
    var playerTwo: Player? = null,

    @CreatedDate
    val createdDate: LocalDateTime = LocalDateTime.now()
)