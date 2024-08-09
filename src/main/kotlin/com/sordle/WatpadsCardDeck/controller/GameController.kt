package com.sordle.WatpadsCardDeck.controller

import com.sordle.WatpadsCardDeck.GameService
import States.Cards

import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

const cardOptions = arrayListOf(Cards.ROCK, Cards.PAPER, Cards.SCISSORS)


@Service
class GameResponse(    
  private val gameService: GameService;
  ) : TextWebSocketHandler() {
   // private var revealCardTo: Player? = null
   private val logger: Logger = LoggerFactory.getLogger(javaClass)
  

  
  open fun draftCard(session: WebSocketSession, move: PlayerMove) {
    
  }

  override fun afterConnectionEstablished(session: WebSocketSession) {
    session.userName = ""
    session.setState(ClientState.OPENED)
    // usersCounter.increment
  }

  override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
    super.handleTextMessage(session, message)
    // logger.info(session.getId() + " Connected")
    try {
      when (session.state) {
	
      }
    }
    catch (e: Exception) {
      logger.error("Caught an exception during message processing", e)
    }
  }

  
  

}
