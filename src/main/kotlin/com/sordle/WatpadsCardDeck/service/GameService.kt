package com.sordle.watpadsCardDeck.service

import com.sordle.watpadsCardDeck.entity.Game
import com.sordle.watpadsCardDeck.entity.GameStates
import com.sordle.watpadsCardDeck.entity.Player
import com.sordle.watpadsCardDeck.model.GameRequest
import com.sordle.watpadsCardDeck.repository.GameRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val userService: UserService
) {
    /**
     * Creates a game with a single player,
     * leaving it ready to be joined by another player
     */
    fun createGame(gameRequest: GameRequest){
        gameRepository.save(
            Game(
                playerOne = Player(userId = gameRequest.userId)
            )
        )
    }

    /**
     * Finds the oldest game waiting for a player and adds user to it
     */
    @Transactional
    fun joinGame(gameRequest: GameRequest){
        // Ensure user exists
        userService.getUser(gameRequest.userId)

        val openGames = gameRepository.findByGameStateOrderByCreatedDateAsc(GameStates.WaitingForPlayers)
        if (openGames.isEmpty()){
            createGame(gameRequest)
        } else {
            val gameToJoin = openGames[0]
            gameToJoin.playerTwo = Player(userId = gameRequest.userId)
            gameToJoin.gameState = GameStates.SelectingTrump
            gameRepository.save(gameToJoin)
        }
    }
}