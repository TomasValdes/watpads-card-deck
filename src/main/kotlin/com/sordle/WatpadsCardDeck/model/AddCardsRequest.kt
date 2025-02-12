package com.sordle.watpadsCardDeck.model

import com.google.gson.JsonObject

/**
 * Message sent in the drafting cards phase to add to starting deck
 *
 * Syntax: {"card" : [[Cards]]}
 */
data class AddCardsRequest (
    val cardsToAdd : List<Cards>
){
    constructor(jsonObject: JsonObject) : this(
        cardsToAdd = jsonObject.getAsJsonArray("card").map { c -> Cards.valueOf(c.asString) }
    )
}