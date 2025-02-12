package com.sordle.watpadsCardDeck.service

import com.sordle.watpadsCardDeck.entity.Game
import com.sordle.watpadsCardDeck.entity.Lobby
import com.sordle.watpadsCardDeck.entity.GameStates
import com.sordle.watpadsCardDeck.entity.Player
import com.sordle.watpadsCardDeck.model.*
import com.sordle.watpadsCardDeck.repository.LobbyRepository
import com.sordle.watpadsCardDeck.repository.GameRepository
import com.sordle.watpadsCardDeck.util.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.socket.WebSocketSession

@Service
class GameService(
    private val lobbyRepository: LobbyRepository,
    private val gameRepository: GameRepository,
    private val userService: UserService,
    private val gameSessionManager: SessionManager
) {
    private final val numCardsAddedByEachPlayer = 3
    private final val numCardsInStartingDeck = 3 + numCardsAddedByEachPlayer * 2
    private final val fullHandSize = 3

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Finds the oldest queued game waiting for a player or create one if it doesn't exist.
     * Joining game is accomplished through connecting to websocket with returned gameId
     */
    @Transactional
    fun getGameToJoin() : Lobby{
        val openGames = lobbyRepository.findAllByOrderByCreatedDateAsc()
        return if (openGames.isEmpty()){
            createLobby()
        } else {
            openGames[0]
        }
    }

    /**
     * Inserts a player into a lobby after a successful websocket connection or
     * to a game if joining the lobby would cause it to be full
     */
    @Transactional(propagation = Propagation.MANDATORY)
    fun joinGame(session: WebSocketSession){
        val lobby = getLobby(session.gameId)!!
        if (lobby.playerOne == null){
            lobby.playerOne = Player(
                user = userService.getUser(session.userId)
            )
        } else{
            lobby.playerTwo = Player(
                user = userService.getUser(session.userId)
            )

            val game = gameRepository.save(Game(lobby))

            lobbyRepository.delete(lobby)

            gameSessionManager.sendMessageToGame(session.gameId, GameResponse(game))
        }
    }

    fun getGame(gameId: Long): Game? {
        val game = gameRepository.findById(gameId).orElse(null)
        if (game == null)
            logger.warn("No game found for gameId: $gameId")
        return game
    }

    fun getLobby(gameId: Long): Lobby? {
        val lobby = lobbyRepository.findById(gameId).orElse(null)
        if (lobby == null)
            logger.warn("No lobby found for gameId: $gameId")
        return lobby
    }

    /**
     * Sets the trump for a player in a game both gotten from the given session
     */
    @Transactional(propagation = Propagation.MANDATORY)
    fun setTrump(session: WebSocketSession, playCardRequest: PlayCardRequest){
        val game = getGame(session.gameId)!!
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
    @Transactional(propagation = Propagation.MANDATORY)
    fun addCardsToDeck(session: WebSocketSession, addCardsRequest: AddCardsRequest){
        val game = getGame(session.gameId)!!
        val player = findPlayerInGame(game, session.userId)

        if (player.cardsAddedToDeck.isEmpty() && addCardsRequest.cardsToAdd.size == numCardsAddedByEachPlayer) {
            player.cardsAddedToDeck.addAll(addCardsRequest.cardsToAdd)
            game.startingDeck.addAll(addCardsRequest.cardsToAdd)
        } else {
            logger.warn("Cannot add cards $addCardsRequest in request by user $session.userId")
        }

        if (game.startingDeck.size >= numCardsInStartingDeck) {
            game.currentDeck.addAll(game.startingDeck)
            distributeHands(game)
            game.gameState = GameStates.PlayingCards
            gameSessionManager.sendMessageToGame(game.gameId, GameResponse(game))
        }
    }

    /**
     * Handles logic for playing given card for a player in a game both gotten from the given session
     */
    @Transactional(propagation = Propagation.MANDATORY)
    fun playCard(session: WebSocketSession, playCardRequest: PlayCardRequest){
        val game = getGame(session.gameId)!!
        val player = findPlayerInGame(game, session.userId)
        if (player.move == null){
            player.hand.remove(playCardRequest.card)
            player.move = playCardRequest.card
        }

        if (game.playerOne.move != null && game.playerTwo.move != null) {
            game.gameState = GameStates.RevealingCards
            gameSessionManager.sendMessageToGame(game.gameId, GameResponse(game))
            gameRepository.save(game)
            evaluateMoves(game)
        }
    }

    /**
     * Creates a game with a single player,
     * leaving it ready to be joined by another player
     */
    private fun createLobby() : Lobby{
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