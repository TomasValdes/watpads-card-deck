package com.sordle.WatpadsCardDeck.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import com.sordle.watpadsCardDeck.

import com.sordle.WatpadsCardDeck.States.Cards

@JsonNaming(ProperyNamingStrategies.SnakeCaseStrategy::class)
data class PlayerMove (
  @get:NotBlank
  val userId: Long = 0,
  @get:NotBlank
  val cardAdd: Card
)
