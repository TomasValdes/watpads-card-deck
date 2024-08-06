package com.sordle.watpadsCardDeck.service

import com.sordle.watpadsCardDeck.entity.User
import com.sordle.watpadsCardDeck.model.UserRequest
import com.sordle.watpadsCardDeck.model.toEntity
import com.sordle.watpadsCardDeck.repository.UserRepository
import io.mockk.*
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals

class UserServiceTest {
    private val userRepository: UserRepository = mockk()
    private val userService = UserService(userRepository)

    private val testUser = User(
        userId = 213412,
        userName = "Test Name"
    )

    private val testUserRequest = UserRequest(
        userName = "New Username"
    )

    @Test
    fun `create test user`() {
        every { userRepository.save(testUserRequest.toEntity()) } answers { testUser }

        userService.createUser(testUserRequest)

        verify(exactly = 1) { userRepository.save(testUserRequest.toEntity()) }
    }

    @Test
    fun `get test user`() {
        every { userRepository.findByIdOrNull(testUser.userId) } returns testUser
        val result = userService.getUser(testUser.userId)

        verify(exactly = 1) { userRepository.findByIdOrNull(testUser.userId) }

        assertEquals(testUser, result)
    }

}