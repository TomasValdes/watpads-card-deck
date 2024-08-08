package com.sordle.WatpadsCardDeck.controller

import com.sordle.WatpadsCardDeck.GameService
import States.Cards

const cardOptions = arrayListOf(Cards.ROCK, Cards.PAPER, Cards.SCISSORS)

@RestController
@RequestMapping("game")
class GameResponse(    
  private val gameService: GameService;
  ) {
   // private var revealCardTo: Player? = null
  

  
  open fun draftCard(id: Integer) {
    
  }

  
  

}
