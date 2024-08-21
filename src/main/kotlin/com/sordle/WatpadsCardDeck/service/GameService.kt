package com.sordle.WatpardsCardDeck.service

import com.sordle.WatpadsCardDeck.model.GameState
import com.sordle.WatpadsCardDeck.States.GameResult
import com.sordle.WatpadsCardDeck.States.Cards

import com.sordle.WatpadsCardDeck.model.GameResponse
import com.sordle.WatpadsCardDeck.entity.User
import com.sordle.WatpadsCardDeck.

import org.springframework.web.socket.WebSocketSession
import org.springframework.context.ApplicationContext

import com.sordle.WatpadsCardDeck.model.PlayerMove

import kotlin.collections.ArrayList
import kotlin.collections.LinkedList

const val HAND_SIZE: Integer = 3;
const val CARDS_TO_REVEAL: Integer = 2;


public interface GameService(private val appContext: ApplicationContext) {
  fun start(session: WebSocketSession): GameResponse
  fun joinGame(session: WebSocketSession, gameId: Long) : GameResponse
  fun draftCardToDeck(session: WebSocketSession, move: PlayerMove) 
  fun selectTrump(session: WebSocketSession, move: PlayerMove)
  fun playCard(session: WebSocketSession, move: PlayerMove)
  fun playAgain(session: WebSocketSession, userId: Long)
  fun deleteGame(session: WebSocketSession)
  fun leaveGame(session: WebSocketSession, userId: Long)
}

class GameService(private val appContext: ApplicationContext) {
  // should this be Player? or String? or id?
  private var userToGame: Hashmap<String, Game>  


  /**
  * Sets the object to its default states
  */
  init {
    playAgain()
  }

  /**
  * @param player the player who is selecting their win condition
  * @param card the "trump suit"
  */
  open fun selectTrump(session: WebSocketSession, card: Cards) {
    val userName = session.userName
    player.winCondition = card;
    if (p1.winCondition != null && p2.winCondition != null) {
      state = CARD_DRAFT
    }
  }

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

    open fun joinRandom(Player player) {
      
    }

    open fun joinRoomByNum(session: WebSocketSession, player: Player, id: Int) {

    }

    open fun joinBotRoom(session: WebSocketSession, player: Player) {
      
    }

    open fun joinRandomRoom(session: WebSocketSession, player: Player) {
      
    }

    open fun readyUp(session: WebSocketSession, player: Player) {
      //TODO: assert in REVEALING_CARDS_PHASE
      state = GameState.PLAYING_CARDS
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
   * Determines the winner of the standard game without taking into account win conditions
   * @return the winner according to the decision matrix as a result reflecting the order of the parameters
   */ 
    private fun GameResult getWinner() {
      return movesToResult[p1.move][p2.move]; 
    }

    open fun selectCard(session: WebSocketSession, player: Player, card: Card) {
    val userName = session.userName
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
}
