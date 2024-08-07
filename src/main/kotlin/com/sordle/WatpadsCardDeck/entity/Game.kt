package com.WatpardsCardDeck

import States.GameState
import States.GameResult
import States.Cards

import kotlin.collections.ArrayList
import kotlin.collections.LinkedList

const val HAND_SIZE: Integer = 3;
const val CARDS_TO_REVEAL: Integer = 2;

class Game(private const Player: p1, private const p2: Player) { 
  var startingDeck: LinkedList<Cards> = LinkedList(cardOptions) // the starting deck at the beginning of the round
  var currentDeck: LinkedList<Cards> = LinkedList() // the current deck being held
  var revealCardTo: Player? = null // which player to reveal the card to
  var state: GameState = GameState.SELECTING_TRUMP // The current state of the game
  var cardCounter: Short = 0 // should be a value between 0 -> HAND_SIZE

}
