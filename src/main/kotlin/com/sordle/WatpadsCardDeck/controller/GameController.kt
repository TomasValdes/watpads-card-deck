package com.sordle.watpadsCardDeck.controller

import com.sordle.watpadsCardDeck.service.GameService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping

/**
 * Used for RESTFUL requests related to the game.
 */
@RestController
@RequestMapping("game")
class GameController(
        private val gameService: GameService
){
    @GetMapping()
    fun getGameToJoin(): ResponseEntity<Long>{
        return ResponseEntity.ok(gameService.getGameToJoin())
    }
}