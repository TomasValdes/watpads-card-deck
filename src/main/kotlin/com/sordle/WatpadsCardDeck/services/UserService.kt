package com.sordle.watpadsCardDeck.services

import com.sordle.watpadsCardDeck.exception.UserNotFoundException
import com.sordle.watpadsCardDeck.models.User
import com.sordle.watpadsCardDeck.repository.UserRepository
import org.springframework.stereotype.Service


@Service
class UserService (
        private val userProfileRepository: UserRepository
){
    fun getUser(userId : Long): User{
        val user = userProfileRepository.findById(userId).orElse(null)
        user?: throw UserNotFoundException("No user found with provided Id")
        return user
    }

    fun createUser(user : User) {
        userProfileRepository.save(user)
    }
}