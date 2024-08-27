package com.sordle.watpadsCardDeck.repository

import com.sordle.watpadsCardDeck.entity.Game
import com.sordle.watpadsCardDeck.entity.GameStates
import org.springframework.data.jpa.repository.JpaRepository

interface GameRepository : JpaRepository<Game, Long> {
    fun findByGameStateOrderByCreatedDateAsc(gameState: GameStates): List<Game>
}