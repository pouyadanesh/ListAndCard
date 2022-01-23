package com.danesh.listandcard.data.model

data class PokemonListResponse(
    val cards: MutableList<Pokemon>,
    val status: String,
    val totalResults: Int
)