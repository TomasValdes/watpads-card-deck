package com.WatpardsCardDeck

import States.GameState
import States.GameResult
import States.Cards

import kotlin.collections.ArrayList
import kotlin.collections.LinkedList

const val HAND_SIZE: Integer = 3;
const val CARDS_TO_REVEAL: Integer = 2;

class Game(private const Player: p1, private const p2: Player) {
  // entity and bean
  private var startingDeck: LinkedList<Cards> = LinkedList(cardOptions)
  private var currentDeck: LinkedList<Cards> = LinkedList()
  
  private var state: GameState = GameState.SELECTING_TRUMP
 

  // bean only
 private var cardCounter: Short = 0
  private var revealCardTo: Player? = null
  private val movesToResult = arrayOf(
      arrayOf(RESULT.TIE, RESULT.P2, RESULT.P1),
      arrayOf(RESULT.P1, RESULT.TIE, RESULT.P2),
      arrayOf(RESULT.P2, RESULT.P1, RESULT.TIE)
  )

  private var highestId: Int // stores the higher of the two ID's
  private var players: Player[2]
  

  /**
  * Sets the object to its default states
  */
  init {
    playAgain()
  }
   
    /**
     * distributes the cards between the players at random
     */
    private fun distributeCards() {
        startingDeck.shuffle()
        currentDeck = LinkedList(startingDeck)
        for (i in 0 until HAND_SIZE) {
            p1.hand.add(currentDeck.pop())
            p2.hand.add(currentDeck.pop())
        }
    }

    /**
     * Determines the winner of the trick
     * @return the winner of the trick. updates game state according to whether the game should keep going or not
     */
    fun getWinner(): Player? {
        val winner = getWinner(p1, p2)
        val success: Player?

        when (winner) {
            GameResult.TIE -> {
                if (p1.hand.isEmpty()) { // p1 and p2 should have the same hand size here
                    distributeCards()
                }
                return null
            }
            GameResult.P1 -> success = p1
            GameResult.P2 -> success = p2
        }

        // assert success != null
        if (success.move == success.winningCard) {
            state = GameState.ROUND_RESULTS
            success.score++
            return success
        } else {
            revealCardTo = if (success == p1) p1 else p2
            state = GameState.REVEAL_CARDS
            return null
        }
    }

    /**
   * @param p1 player 1 relective in {@link #RESULT}
   * @param p2 player 2 reflective in {@link #RESULT}
   * Determines the winner of the standard game without taking into account win conditions.
   * @return the winner according to the decision matrix as a result reflecting the order of the parameters
   */ 
    private fun GameResult getWinner() {
      return movesToResult[p1.move][p2.move]; 
    }

    /**
    * @param player the player who selects the card
    * @param card the card to be selected
    * Select a card at some point in the game state
    */
    open fun selectCard(player: Player, card: Card) {
      when (state) {
	GameState.SELECTING_TRUMP -> {
	  player.winCondition = card;
	  if (p1.winCondition != null && p2.winCondition != null) {
	    state = CARD_DRAFT
	  }
	}

	GameState.CARD_DRAFT -> {
	  startingDeck.add(card)
	  cardCounter++
	  if (cardCounter == HAND_SIZE * 2) {
            distributeCards()
            state = GameState.PLAYING_CARDS
            cardCounter = 0
	  }
	}

	GameState.PLAYING_CARDS -> {
	  if (!player.hand.contains(card)) {
	    return
	  }
      
	  var other: Player
	  if (player == p1) {
	    other = p2
	  }
	  else {
	    other = p1
	  }

	  if (player.hand.size() < other.hand.size()) {
	    return
	  }

	  player.move = player.hand.remove(card);

	  if (p1.hand.isEmpty() && p2.hand.isEmpty()) {
	    state = TRICK_RESULTS
	  }
	}
      }
    }


  /**
   * @param player the player who is playing the card
   * @param index the index of the card in the players hand to remove
   * plays the card and removes it from the player's hand
   */ 
    open fun playCard(player: Player, index: Int) {
      if (index >= player.hand.size()) {
	return
      }
      
      var other: Player
      if (player == p1) {
	other = p2
      }
      else {
	other = p1
      }

      if (player.hand.size() < other.hand.size()) {
	return
      }

      player.move = player.hand.removeAt(index);

      if (p1.hand.isEmpty() && p2.hand.isEmpty()) {
	state = TRICK_RESULTS
      }
    }

    /**
    * get options of what can be drafted
    */
    fun getDraftOptions(): List<Cards> {
     return cardOptions; 
    }

    fun getRevealedCards(player: Player): List<Cards>? {
      return if (player == revealCardTo) {
	state = GameState.PLAYING_CARDS
	currentDeck.subList(0, CARDS_TO_REVEAL);
      }
      else {
	null
      }
    }

    /**
    * play the game again?
    */
    fun playAgain() {
      // update db with score here

      state = GameState.SELECTING_TRUMP;
      startingDeck = new LinkedList<>(cardOptions); 

      // reset the players in case not already
      p1.winCondition = null
      p2.winCondition = null
      revealCardTo = null
    }

    private fun getPlayerFromID(id: Int): Player {
      return players[id / highestID]
    }
}
