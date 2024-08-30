package com.sordle.watpadsCardDeck.util

import com.google.gson.JsonObject
import com.google.gson.JsonParser

/**
 * Util methods that help with converting objects
 */
object Conversion {
    fun textToJson(stringJson: String): JsonObject = JsonParser.parseString(stringJson).asJsonObject
}