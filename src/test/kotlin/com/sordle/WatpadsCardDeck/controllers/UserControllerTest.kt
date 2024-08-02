package com.sordle.watpadsCardDeck.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.sordle.watpadsCardDeck.exception.UserNotFoundException
import com.sordle.watpadsCardDeck.models.User
import com.sordle.watpadsCardDeck.services.UserService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class UserControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockBean
    lateinit var userService: UserService
    private val objectMapper = ObjectMapper()

    private val testUser = User(
        userId = 213412,
        userName = "Test Name"
    )

    @Test
    fun `create user returns 201`() {
        val userJson = objectMapper.writeValueAsString(testUser)

        every { userService.createUser(testUser) } returns Unit

        mockMvc.perform(
            post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        )
            .andExpect(status().isCreated)
    }

    @Test
    fun `create user returns 400`() {
        val invalidUser = User(userId = -5, userName = "")
        val userJson = objectMapper.writeValueAsString(invalidUser)

        mockMvc.perform(
            post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `get user returns 200`() {
        every { userService.getUser(testUser.userId) } returns testUser

        mockMvc.perform(
            get("/user/${testUser.userId}")
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `get user returns 404`() {
        every { userService.getUser(testUser.userId) } throws UserNotFoundException("No user found with provided Id")

        mockMvc.perform(
            get("/user/${testUser.userId}")
        )
            .andExpect(status().isNotFound)
    }
}
