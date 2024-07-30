package com.sordle.watpadsCardDeck.controller

import com.sordle.watpadsCardDeck.models.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.sordle.watpadsCardDeck.services.LoginService
import org.springframework.web.bind.annotation.PathVariable
import java.util.UUID

@RestController
@RequestMapping("user")
public class UserController(
        private val loginService: LoginService
){


    @GetMapping("/{userId}")
    fun getUser( @PathVariable("userId") userId: UUID): User {
        return loginService.getUser(userId)
    }
}