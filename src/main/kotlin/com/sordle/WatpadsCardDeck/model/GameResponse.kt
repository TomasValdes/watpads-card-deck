package com.sordle.WatpadsCardDeck.model

import States.Cards

import kotlin.collections.LinkedList
import kotlin.collections.ArrayList
import com.sordle.WatpadsCardDeck.States.Cards
import com.sordle.WatpadsCardDeck.State.GameState
import com.sordle.WatpadsCardDeck.model.Score

const cardOptions = arrayListOf(Cards.ROCK, Cards.PAPER, Cards.SCISSORS)

class GameResponse(
   private val deck: LinkedList<Cards>
   private val gameState: GameState 
   private val scores: Scores

)
  

}
