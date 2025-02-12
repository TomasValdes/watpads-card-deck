package com.sordle.watpadsCardDeck.repository

import com.sordle.watpadsCardDeck.entity.Game
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface GameRepository : JpaRepository<Game, Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findGameByGameId(gameId: Long): Game?
}