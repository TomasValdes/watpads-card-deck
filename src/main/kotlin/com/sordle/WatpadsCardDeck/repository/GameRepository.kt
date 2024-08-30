package com.sordle.watpadsCardDeck.repository

import com.sordle.watpadsCardDeck.entity.Game
import org.springframework.data.jpa.repository.JpaRepository

interface GameRepository : JpaRepository<Game, Long>