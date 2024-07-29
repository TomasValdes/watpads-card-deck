package com.sordle.WatpadsCardDeck

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

class GameController() : TexxtWebSocketHandler() {
  private val logger: Logger = LoggerFactory.getLogger(javaClass)

  

}
