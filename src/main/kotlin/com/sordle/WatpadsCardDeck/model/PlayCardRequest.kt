package com.sordle.watpadsCardDeck.model

import com.google.gson.JsonObject
import jakarta.validation.constraints.NotBlank

/**
 * Message sent in the play card phase to play provided card
 *
 * Syntax: {"card" : "[card]"}
 */
data class PlayCardRequest (
    @get:NotBlank
    val card : Cards
) {
    constructor(jsonObject: JsonObject) : this(
        card = Cards.valueOf(jsonObject.get("card").asString)
    )

}