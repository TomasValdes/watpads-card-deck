package com.sordle.WatpadsCardDeck.Exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class GameNotFoundException(
  statusCode: Int = 512,
  errorMessage: String
  ) : ApplicationException(statusCode, errorMessage)
