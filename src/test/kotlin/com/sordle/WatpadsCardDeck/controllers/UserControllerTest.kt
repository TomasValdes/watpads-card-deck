package com.sordle.watpadsCardDeck.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.sordle.watpadsCardDeck.exception.NotFoundException
import com.sordle.watpadsCardDeck.entity.User
import com.sordle.watpadsCardDeck.service.UserService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import com.ninjasquad.springmockk.MockkBean
import com.sordle.watpadsCardDeck.model.UserRequest
import io.mockk.just
import io.mockk.runs
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class UserControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    lateinit var userService: UserService
    private val objectMapper = ObjectMapper()
    private val testUser = User(
        userId = 213412,
        userName = "Test Name"
    )


    @Test
    fun `create user returns 201`() {
        val testUserRequest = UserRequest(userName = testUser.userName)
        val userRequestJson = objectMapper.writeValueAsString(testUserRequest)

        every { userService.createUser(testUserRequest) } just runs

        mockMvc.perform(
            post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userRequestJson)
        )
            .andExpect(status().isCreated)
    }

    @Test
    fun `create user returns 400`() {
        val invalidUserRequest = UserRequest(userName = "")
        val userJson = objectMapper.writeValueAsString(invalidUserRequest)

        every { userService.createUser(invalidUserRequest) } returns Unit

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
        every { userService.getUser(testUser.userId) } throws NotFoundException(errorMessage =  "No user found with provided Id")

        mockMvc.perform(
            get("/user/${testUser.userId}")
        )
            .andExpect(status().isNotFound)
    }
}
