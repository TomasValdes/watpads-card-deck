package com.sordle.watpadsCardDeck.controller

import com.sordle.watpadsCardDeck.model.GameRequest
import com.sordle.watpadsCardDeck.service.GameService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("game")
class GameController(
        private val gameService: GameService
){
    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun joinGame( @RequestBody @Valid gameRequest: GameRequest): ResponseEntity<String>{
        gameService.joinGame(gameRequest)
        return ResponseEntity.status(200).build()
    }
}