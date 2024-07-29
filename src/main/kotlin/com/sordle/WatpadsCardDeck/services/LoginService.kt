package com.sordle.watpadsCardDeck.services

import com.sordle.watpadsCardDeck.models.User
import com.sordle.watpadsCardDeck.repository.UserProfileRepository
import org.springframework.stereotype.Service
import java.util.UUID


@Service
class LoginService (
        private val userProfileRepository: UserProfileRepository
){
    fun getUser(userId : UUID): User{
        return userProfileRepository.findById(userId).orElse(null)
    }
}