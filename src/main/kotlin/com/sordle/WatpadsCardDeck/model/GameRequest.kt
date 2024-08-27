package com.sordle.watpadsCardDeck.model

import jakarta.validation.constraints.Min

data class GameRequest (
    @get:Min(value = 1)
    val userId: Long,
    )