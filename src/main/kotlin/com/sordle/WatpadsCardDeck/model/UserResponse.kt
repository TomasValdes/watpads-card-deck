package com.sordle.watpadsCardDeck.model

import com.sordle.watpadsCardDeck.entity.User
import jakarta.validation.constraints.NotBlank

data class UserResponse (
    @get:NotBlank
    val userId: Long,

    @get:NotBlank
    val userName: String
) {
    constructor(user : User) : this(
        userId = user.userId,
        userName = user.userName
    )
}