package com.sordle.watpadsCardDeck.controllers

import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import com.ninjasquad.springmockk.MockkBean
import com.sordle.watpadsCardDeck.TestConfig
import com.sordle.watpadsCardDeck.service.GameService
import com.sordle.watpadsCardDeck.service.UserService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class GameControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    lateinit var userService: UserService
    @MockkBean
    lateinit var gameService: GameService

    private val testConfig = TestConfig()

    @Test
    fun `get game to join returns 200`() {
        every { gameService.getGameToJoin() } returns testConfig.testLobby

        mockMvc.perform(
            get("/game")
        )
            .andExpect(status().isOk)
    }
}
