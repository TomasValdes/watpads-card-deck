package com.sordle.watpadsCardDeck.services

import com.sordle.watpadsCardDeck.models.User
import com.sordle.watpadsCardDeck.repository.UserRepository
import io.mockk.every
import org.junit.jupiter.api.Test
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals

class UserServiceTest {
    private val userRepository: UserRepository = mockk()
    private val userService = UserService(userRepository)

    private val testUser = User(
        userId = 213412,
        userName = "Test Name"
    )

    @Test
    fun `create test user`() {
        every { userService.createUser(testUser) } returns Unit

        userService.createUser(testUser)

        verify(exactly = 1) { userRepository.save(testUser) }
    }

    @Test
    fun `get test user`() {
        every  { userRepository.findByIdOrNull(testUser.userId) } returns testUser
        val result = userService.getUser(testUser.userId)

        verify(exactly = 1) { userRepository.findByIdOrNull(testUser.userId) }

        assertEquals(testUser, result)
    }

}