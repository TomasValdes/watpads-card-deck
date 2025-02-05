package com.sordle.watpadsCardDeck.model

import com.google.gson.JsonObject
import jakarta.validation.constraints.NotBlank

/**
 * Message sent in the drafting cards phase to add to starting deck
 *
 * Syntax: {"card" : [[card]]}
 */
data class AddCardsRequest (
    @get:NotBlank
    val cardsToAdd : List<Cards>
){
    constructor(jsonObject: JsonObject) : this(
        cardsToAdd = jsonObject.getAsJsonArray("card").map { c -> Cards.valueOf(c.asString) }
    )
}