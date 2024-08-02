package com.sordle.watpadsCardDeck.models

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.persistence.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Entity
data class User (
        @Id
        @GeneratedValue(strategy= GenerationType.AUTO)
        val userId: Long = 0,

        @Column(unique = true, nullable = false)
        val userName: String = ""
)