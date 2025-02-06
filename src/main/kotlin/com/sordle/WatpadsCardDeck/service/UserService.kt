package com.sordle.watpadsCardDeck.service

import com.sordle.watpadsCardDeck.exception.NotFoundException
import com.sordle.watpadsCardDeck.entity.User
import com.sordle.watpadsCardDeck.model.UserRequest
import com.sordle.watpadsCardDeck.model.toEntity
import com.sordle.watpadsCardDeck.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService (
        private val userProfileRepository: UserRepository
){
    @Transactional
    fun getUser(userId : Long): User {
        val user = userProfileRepository.findById(userId).orElse(null)
        user?: throw NotFoundException(errorMessage =  "No user found with provided Id")
        return user
    }

    fun createUser(userRequest : UserRequest) {
        userProfileRepository.save(userRequest.toEntity())
    }

    /**
     * Temporary method for testing. Rework needed for creating user who doesn't log in.
     */
    fun createAnonymousUser(): User {
        return userProfileRepository.save(
            User(
                userName = "Anonymous User " + UUID.randomUUID().toString(),
            )
        )
    }
}