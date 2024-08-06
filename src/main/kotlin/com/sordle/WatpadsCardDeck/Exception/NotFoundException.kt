package com.sordle.watpadsCardDeck.exception


import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException(
    statusCode: Int = 404,
    errorMessage: String
) : ApplicationException(statusCode, errorMessage)