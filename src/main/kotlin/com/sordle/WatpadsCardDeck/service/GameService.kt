package com.sordle.watpadsCardDeck.service

import com.sordle.watpadsCardDeck.entity.Game
import com.sordle.watpadsCardDeck.entity.Lobby
import com.sordle.watpadsCardDeck.entity.GameStates
import com.sordle.watpadsCardDeck.entity.Player
import com.sordle.watpadsCardDeck.exception.NotFoundException
import com.sordle.watpadsCardDeck.model.*
import com.sordle.watpadsCardDeck.repository.LobbyRepository
import com.sordle.watpadsCardDeck.repository.GameRepository
import com.sordle.watpadsCardDeck.util.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.socket.WebSocketSession

@Service
class GameService(
    private val lobbyRepository: LobbyRepository,
    private val gameRepository: GameRepository,
    private val userService: UserService,
    private val gameSessionManager: SessionManager
) {
    private final val numCardsInStartingDeck = 9
    private final val fullHandSize = 3

    /**
     * Finds the oldest queued game waiting for a player or create one if it doesn't exist.
     * Joining game is accomplished through connecting to websocket with returned gameId
     */
    fun getGameToJoin() : Lobby{
        val openGames = lobbyRepository.findAllByOrderByCreatedDateAsc()
        return if (openGames.isEmpty()){
            createNewGameInQueue()
        } else {
            openGames[0]
        }
    }

    /**
     * Inserts a player into a lobby after a successful websocket connection or
     * to a game if joining the lobby would cause it to be full
     */
    @Transactional
    fun joinGame(session: WebSocketSession){
        val lobby = session.game as Lobby
        if (lobby.playerOne == null){
            lobby.playerOne = Player(
                user = userService.getUser(session.userId)
            )
        } else{
            lobby.playerTwo = Player(
                user = userService.getUser(session.userId)
            )

            session.setGame(
                gameRepository.save(
                    Game(lobby)
                )
            )

            lobbyRepository.delete(lobby)

            gameSessionManager.sendMessageToGame(session.game.gameId, GameResponse(session.game as Game))
        }
    }

    fun getGame(gameId: Long): Game{
        val game = gameRepository.findById(gameId).orElse(null)
        game?: throw NotFoundException(errorMessage =  "No game found with provided Id")
        return game
    }

    fun getQueuedGame(gameId: Long): Lobby{
        val gameQueue = lobbyRepository.findById(gameId).orElse(null)
        gameQueue?: throw NotFoundException(errorMessage =  "No game queue found with provided Id")
        return gameQueue
    }

    /**
     * Sets the trump for a player in a game both gotten from the given session
     */
    @Transactional
    fun setTrump(session: WebSocketSession, playCardRequest: PlayCardRequest){
        val game = session.game as Game
        val player = findPlayerInGame(game, session.userId)

        if (player.trumpCard == null){
            player.trumpCard = playCardRequest.card
        }

        if (game.playerOne.trumpCard != null && game.playerTwo.trumpCard != null){
            game.gameState = GameStates.DraftingCards
            gameSessionManager.sendMessageToGame(game.gameId, GameResponse(game))
        }
    }

    /**
     * Adds given cards to starter deck for a game from the given session
     */
    @Transactional
    fun addCardsToDeck(session: WebSocketSession, addCardsRequest: AddCardsRequest){
        val game = session.game as Game
        val player = findPlayerInGame(game, session.userId)

        if (player.cardsAddedToDeck.isEmpty()) {
            player.cardsAddedToDeck.addAll(addCardsRequest.cardsToAdd)
            game.startingDeck.addAll(addCardsRequest.cardsToAdd)
        }

        if (game.startingDeck.size >= numCardsInStartingDeck &&
            game.playerOne.cardsAddedToDeck.isNotEmpty() && game.playerTwo.cardsAddedToDeck.isNotEmpty()) {
            game.currentDeck.addAll(game.startingDeck)
            distributeHands(game)
            game.gameState = GameStates.PlayingCards
            gameSessionManager.sendMessageToGame(game.gameId, GameResponse(game))
        }
    }

    /**
     * Handles logic for playing given card for a player in a game both gotten from the given session
     */
    @Transactional
    fun playCard(session: WebSocketSession, playCardRequest: PlayCardRequest){
        val game = session.game as Game
        val player = findPlayerInGame(game, session.userId)
        if (player.move == null){
            player.hand.remove(playCardRequest.card)
            player.move = playCardRequest.card
        }

        if (game.playerOne.move != null && game.playerTwo.move != null) {
            game.gameState = GameStates.RevealingCards
            gameSessionManager.sendMessageToGame(game.gameId, GameResponse(game))
            evaluateMoves(game)
        }
    }

    /**
     * Creates a game with a single player,
     * leaving it ready to be joined by another player
     */
    private fun createNewGameInQueue() : Lobby{
        return lobbyRepository.save(
            Lobby()
        )
    }

    /**
     * Shuffles deck then sends each player their new hand
     */
    private fun distributeHands(game: Game){
        game.currentDeck.shuffle()

        // Distribute cards to players
        for (i in 0 until fullHandSize) {
            game.playerOne.hand.add(game.currentDeck.removeFirst())
            game.playerTwo.hand.add(game.currentDeck.removeFirst())
        }

        for (session in gameSessionManager.getSessions(game.gameId)!!){
            val message = PlayerResponse(
                hand = findPlayerInGame(game, session.userId).hand
            )
            session.sendObjectMessage(message)
        }
    }

    private fun evaluateMoves(game: Game){
        val playerOne = game.playerOne
        val playerTwo = game.playerTwo

        // Handle tie
        if (playerOne.move == playerTwo.move) {
            game.gameState = GameStates.PlayingCards
            gameSessionManager.sendMessageToGame(game.gameId, GameResponse(game))
        }
        // Handle playerOne victory
        else if (didPlayerOneWinRound(game)){
            if (playerOne.move == playerOne.trumpCard){
                handlePlayerVictory(playerOne, game)
            } else {
                
            }
        }
        // Handle playerTwo victory
        else {
            if (playerTwo.move == playerTwo.trumpCard){
                handlePlayerVictory(playerTwo, game)
            } else{

            }
        }

        playerOne.move = null
        playerTwo.move = null
    }

    private fun didPlayerOneWinRound(game: Game): Boolean {
        val playerOneMove = game.playerOne.move
        val playerTwoMove = game.playerTwo.move
        return (playerOneMove == Cards.Rock && playerTwoMove == Cards.Scissors)
                || (playerOneMove == Cards.Paper && playerTwoMove == Cards.Rock)
                || (playerOneMove == Cards.Scissors && playerTwoMove == Cards.Paper)
    }

    private fun handlePlayerVictory(player: Player, game: Game){
        game.winner = player
        game.gameState = GameStates.GameResults
        gameSessionManager.sendMessageToGame(game.gameId, GameResponse(game))
        gameSessionManager.endGameSessions(game.gameId)
    }

    private fun findPlayerInGame(game: Game, userId: Long): Player{
        return if (game.playerOne.user.userId == userId){
            game.playerOne
        } else if (game.playerTwo.user.userId == userId){
            game.playerTwo
        } else{
            throw NoSuchElementException("No player with ID $userId in game with ID ${game.gameId}")
        }
    }
}