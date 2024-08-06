package com.sordle.watpadsCardDeck.exception

open class ApplicationException(
    private val statusCode: Int,
    private val errorMessage: String
) : RuntimeException(
    errorMessage
)