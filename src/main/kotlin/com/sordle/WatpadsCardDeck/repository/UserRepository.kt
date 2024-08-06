package com.sordle.watpadsCardDeck.repository

import com.sordle.watpadsCardDeck.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

}