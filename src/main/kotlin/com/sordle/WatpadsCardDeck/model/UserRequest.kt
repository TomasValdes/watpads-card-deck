package com.sordle.watpadsCardDeck.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.sordle.watpadsCardDeck.entity.User
import jakarta.validation.constraints.NotBlank

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UserRequest (
    @get:NotBlank
    val userName: String
)

fun UserRequest.toEntity(): User =
    User(
        userName = this.userName
    )