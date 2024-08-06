package com.WatpardsCardDeck

import States.GameState
import States.GameResult
import States.Cards

import kotlin.collections.ArrayList
import kotlin.collections.LinkedList

const val HAND_SIZE: Integer = 3;
const val CARDS_TO_REVEAL: Integer = 2;

class Game(private const Player: p1, private const p2: Player) {
  private val cardOptions = arrayListOf(CARDS.ROCK, CARDS.PAPER, CARDS.SCISSORS) //TODO: look to remove  
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
        val winner = getWinner(player, bot)
        val success: Player?

        when (winner) {
            GameResult.TIE -> {
                if (p1.hand.isEmpty()) {
                    // TODO: if debug: assert bot.hand.isEmpty()
                    distributeCards()
                }
                return null
            }
            GameResult.P1 -> success = player
            GameResult.P2 -> success = bot
        }

        // assert success != null
        if (success.move == success.winningCard) {
            state = GAME_STATE.ROUND_RESULTS
            success.score++
            return success
        } else {
            revealCardTo = if (success == player) player else bot
            state = GAME_STATE.REVEAL_CARDS
            return null
        }
    }

    private fun GameResult getWinner() {
      return movesToResult[p1.move][p2.move]; 
    }

    fun playCard(index: Int) {
      player.move = player.hand.removeAt(index);
    }

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

    fun playAgain() {
      // update db with score here

      state = GameState.CARD_DRAFT;
      startingDeck = new LinkedList<>(cardOptions); 
    }
}
