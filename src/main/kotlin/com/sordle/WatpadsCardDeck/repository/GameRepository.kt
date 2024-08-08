package com.sordle.WatpadsCardDeck.repository

import com.sordle.watpadsCarDeck.entity.Game
import org.springframework.data.jpa.repository.JpaRepository

interface GameRepository : JpaRepository<Game, Long> {
  
}
