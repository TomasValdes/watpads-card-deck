package com.sordle.watpadsCardDeck.model

import com.sordle.watpadsCardDeck.entity.User
import jakarta.validation.constraints.NotBlank

data class UserRequest (
    @get:NotBlank
    val userName: String
)

fun UserRequest.toEntity(): User =
    User(
        userName = this.userName
    )