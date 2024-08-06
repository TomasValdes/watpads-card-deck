package com.sordle.watpadsCardDeck.entity

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Entity
data class User (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val userId: Long = 0,

        @Column(unique = true, nullable = false)
        @get:NotBlank(message = "User name must not be blank")
        val userName: String = ""
)
