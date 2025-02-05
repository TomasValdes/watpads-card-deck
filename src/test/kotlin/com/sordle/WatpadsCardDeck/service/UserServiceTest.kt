package com.sordle.watpadsCardDeck.service

import com.sordle.watpadsCardDeck.TestConfig
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

    private val testConfig = TestConfig()
    private val testUserRequest = UserRequest(
        userName = "New Username"
    )

    @Test
    fun `create test user`() {
        every { userRepository.save(testUserRequest.toEntity()) } answers { testConfig.testUser }

        userService.createUser(testUserRequest)

        verify(exactly = 1) { userRepository.save(testUserRequest.toEntity()) }
    }

    @Test
    fun `get test user`() {
        every { userRepository.findByIdOrNull(testConfig.testUser.userId) } returns testConfig.testUser
        val result = userService.getUser(testConfig.testUser.userId)

        verify(exactly = 1) { userRepository.findByIdOrNull(testConfig.testUser.userId) }

        assertEquals(testConfig.testUser, result)
    }

}