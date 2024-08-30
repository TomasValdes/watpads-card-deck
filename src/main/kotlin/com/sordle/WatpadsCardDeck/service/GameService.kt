package com.sordle.watpadsCardDeck.service

import com.sordle.watpadsCardDeck.entity.Game
import com.sordle.watpadsCardDeck.entity.GameStates
import com.sordle.watpadsCardDeck.entity.Player
import com.sordle.watpadsCardDeck.exception.NotFoundException
import com.sordle.watpadsCardDeck.model.GameResponse
import com.sordle.watpadsCardDeck.repository.GameRepository
import com.sordle.watpadsCardDeck.util.gameId
import com.sordle.watpadsCardDeck.util.sendObjectMessage
import com.sordle.watpadsCardDeck.util.userId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.socket.WebSocketSession

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val userService: UserService
) {
    /**
     * Finds the oldest game waiting for a player or create one if it doesn't exist
     */
    fun findGame() : GameResponse{
        val openGames = gameRepository.findByGameStateOrderByCreatedDateAsc(GameStates.WaitingForPlayers)
        return if (openGames.isEmpty()) {
            createGame()
        } else {
            GameResponse(openGames[0])
        }
    }

    /**
     * Add a player to requested game if possible
     */
    @Transactional
    fun joinGame(session: WebSocketSession){
        val gameToJoin = getGame(session.gameId)
        if (gameToJoin.playerOne != null){
            gameToJoin.playerOne = Player(userId = session.userId)
        } else if (gameToJoin.playerTwo != null){
            gameToJoin.playerTwo = Player(userId = session.userId)
        } else {

        }
        gameToJoin.gameState = GameStates.SelectingTrump
        gameRepository.save(gameToJoin)
        session.sendObjectMessage(GameResponse(gameToJoin))
    }

    /**
    * @param gameId the id to fetch
    * Get game via ID
    * @return an entity for the game
    */
    fun getGame(gameId: Long): Game {
        val game = gameRepository.findById(gameId).orElse(null)
        game?: throw NotFoundException(errorMessage =  "No user found with provided Id")
        return game
    }

    /**
     * Sets the trump for a player in a game both gotten from the given session
     */
    fun setTrump(session: WebSocketSession, card: Cards) { 
      // verify that you are in the proper game mode
      userService.getUser(session.userId)
    }

    /**
     * Adds given cards to starter deck for a game from the given session
     */
    fun addCardsToDeck() {

    }

    /**
     * Handles logic for playing given card for a player in a game both gotten from the given session
     */
    fun playCard() {

    }

    /**
     * Creates a game with a single player,
     * leaving it ready to be joined by another player
     */
    private fun createGame() : GameResponse {
        return GameResponse(
            gameRepository.save(
                Game()
            )
        )
    }
}
