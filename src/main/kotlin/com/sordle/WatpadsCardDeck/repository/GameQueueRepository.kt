package com.sordle.watpadsCardDeck.repository

import com.sordle.watpadsCardDeck.entity.GameQueue
import org.springframework.data.jpa.repository.JpaRepository

interface GameQueueRepository : JpaRepository<GameQueue, Long> {
    fun findAllByOrderByCreatedDateAsc(): List<GameQueue>
}