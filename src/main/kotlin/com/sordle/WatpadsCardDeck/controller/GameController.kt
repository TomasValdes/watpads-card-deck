package com.sordle.watpadsCardDeck.controller

import com.sordle.watpadsCardDeck.model.GameResponse
import com.sordle.watpadsCardDeck.service.GameService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping

@RestController
@RequestMapping("game")
class GameController(
        private val gameService: GameService
){
    @GetMapping()
    fun findGame(): ResponseEntity<GameResponse>{
        return ResponseEntity.ok(gameService.findGame())
    }
}