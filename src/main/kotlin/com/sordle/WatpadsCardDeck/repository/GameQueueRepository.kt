package com.sordle.watpadsCardDeck.repository

import com.sordle.watpadsCardDeck.entity.Lobby
import org.springframework.data.jpa.repository.JpaRepository

interface GameQueueRepository : JpaRepository<Lobby, Long> {
    fun findAllByOrderByCreatedDateAsc(): List<Lobby>
}