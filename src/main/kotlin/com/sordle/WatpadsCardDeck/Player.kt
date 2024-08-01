package com.WatpadsCardDeck
import States.Cards

class Player(
  val: id: Integer,
  val: playerDeck: MutableList<Cards>
  ) {
   val score: Integer = 0;
   val move: Cards;
   val winCondition: Cards;
   val hasPlayed: boolean = false;


  incrimentScore() {
    score++
  }
  

  
}


