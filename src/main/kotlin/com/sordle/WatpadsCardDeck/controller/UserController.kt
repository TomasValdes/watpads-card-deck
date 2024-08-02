package com.sordle.watpadsCardDeck.controller

import com.sordle.watpadsCardDeck.models.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.sordle.watpadsCardDeck.services.UserService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("user")
class UserController(
        private val userService: UserService
){


    @GetMapping("/{userId}")
    fun getUser( @PathVariable("userId") userId: Long): ResponseEntity<User> {
        return ResponseEntity.ok(userService.getUser(userId))
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createUser( @RequestBody @Validated user: User): ResponseEntity<Unit>{
        return ResponseEntity.ok(userService.createUser(user))
    }
}