package com.WatpardsCardDeck

import States.GameState
import States.GameResult
import States.Cards

import kotlin.collections.ArrayList
import kotlin.collections.LinkedList

const val HAND_SIZE: Integer = 3;
const val CARDS_TO_REVEAL: Integer = 2;

class Game(private const Player: p1, private const p2: Player) {
  private val cardOptions = arrayListOf(CARDS.ROCK, CARDS.PAPER, CARDS.SCISSORS)
  private var startingDeck: LinkedList<Cards> = LinkedList(cardOptions)
  private var currentDeck: LinkedList<Cards> = LinkedList()
  private var revealCardTo: Player? = null
  private var state: GameState = GameState.CARD_DRAFT
  private var cardCounter: Short = 0

   
  private val movesToResult = arrayOf(
      arrayOf(RESULT.TIE, RESULT.P2, RESULT.P1),
      arrayOf(RESULT.P1, RESULT.TIE, RESULT.P2),
      arrayOf(RESULT.P2, RESULT.P1, RESULT.TIE)
  )

    /**
     * Drafts the card into the starting deck
     * After the bot drafts its cards, the cards are distributed and the game state changes to playing cards
     */
    open fun draftCard(card: Cards) {
        startingDeck.add(card)
        cardCounter++
        if (cardCounter == HAND_SIZE * 2) {
            distributeCards()
            state = GameState.PLAYING_CARDS
            cardCounter = 0
        }
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
     * Determines the winner of the game and is dependent on win condition.
     * @return the winner of the game and null if neither player has won yet. updates game state if appropriate to do so
     */
    fun getWinner(): Player? {
        val winner = getWinner(p1, p2)
        val success: Player?

        when (winner) {
            GameResult.TIE -> {
                if (p1.hand.isEmpty()) {
                    // TODO: if debug: assert bot.hand.isEmpty()
                    distributeCards()
                }
                return null
            }
            GameResult.P1 -> success = p1
            GameResult.P2 -> success = p2
        }

        // assert success != null
        if (success.move == success.winningCard) {
            state = GAME_STATE.ROUND_RESULTS
            success.score++
            return success
        } else {
            revealCardTo = if (success == p1) p1 else p2
            state = GAME_STATE.REVEAL_CARDS
            return null
        }
    }

    /**
   * @param p1 player 1 relective in {@link #RESULT}
   * @param p2 player 2 reflective in {@link #RESULT}
   * Determines the winner of the standard game without taking into account win conditions
   * @return the winner according to the decision matrix as a result reflecting the order of the parameters
   */ 
    private fun GameResult getWinner() {
      return movesToResult[p1.move][p2.move]; 
    }

  /**
   * @param player the player who is playing the card
   * @param index the index of the card in the players hand to remove
   * plays the card and removes it from the player's hand
   */ 
    fun playCard(player: Player, index: Int) {
      player.move = player.hand.removeAt(index);
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

      state = GameState.CARD_DRAFT;
      startingDeck = new LinkedList<>(cardOptions); 
    }
}
