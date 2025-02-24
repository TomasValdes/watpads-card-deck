package com.sordle.watpadsCardDeck.model

abstract class GenericResponse{
    val responseType = this.javaClass.simpleName
}