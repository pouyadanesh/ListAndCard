package com.danesh.listandcard.repository

import com.danesh.listandcard.data.local.PokemonDao
import com.danesh.listandcard.data.model.Pokemon
import com.danesh.listandcard.data.model.PokemonListResponse
import com.danesh.listandcard.data.remote.PokemonApi
import retrofit2.Response
import javax.inject.Inject

class PokemonRepository@Inject constructor(
    private val pokemonApi: PokemonApi,
    private val pokemonDao: PokemonDao
) {

    suspend fun getCards(pageNumber: Int): Response<PokemonListResponse> {
        return pokemonApi.getPokemons(pageNumber)
    }

    suspend fun updatePokemon(pokemon: Pokemon) = pokemonDao.updatePokemon(pokemon)

    fun getAllPokemons() = pokemonDao.getPokemons()

    suspend fun insertPokemon(pokemon: Pokemon) = pokemonDao.insert(pokemon)

    suspend fun deletePokemon(pokemon: Pokemon) = pokemonDao.delete(pokemon)
}