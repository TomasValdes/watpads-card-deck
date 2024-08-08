package com.sordle.WatpadsCardDeck.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias

class Score(val: userName: String) {
  var totalWins = 0

  // var rank

  fun incrementWins() {
    totalWins++
  }


  //TODO: Maybe score document here
}

