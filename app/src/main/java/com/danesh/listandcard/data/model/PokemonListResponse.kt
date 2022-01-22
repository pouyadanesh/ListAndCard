package com.danesh.listandcard.data.model

data class PokemonListResponse(
    val pokemons: MutableList<Pokemon>,
    val status: String,
    val totalResults: Int
)