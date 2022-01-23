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

    suspend fun getCards(countryCode: String, pageNumber: Int): Response<PokemonListResponse> {
        return pokemonApi.getPokemons(countryCode, pageNumber)
    }

    fun getAllArticles() = pokemonDao.getArticles()

    suspend fun insertArticle(pokemon: Pokemon) = pokemonDao.insert(pokemon)

    suspend fun deleteArticle(pokemon: Pokemon) = pokemonDao.delete(pokemon)
}