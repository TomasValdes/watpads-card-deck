package com.sordle.watpadsCardDeck.repository

import com.sordle.watpadsCardDeck.entity.Lobby
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface LobbyRepository : JpaRepository<Lobby, Long> {
    fun findAllByOrderByCreatedDateAsc(): List<Lobby>
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findLobbyByGameId(gameId: Long): Lobby?
}