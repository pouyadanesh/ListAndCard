package com.danesh.listandcard.repository

import com.danesh.listandcard.data.local.PokemonDao
import com.danesh.listandcard.data.model.Pokemon
import com.danesh.listandcard.data.model.PokemonListResponse
import com.danesh.listandcard.data.remote.PokemonApi
import retrofit2.Response
import javax.inject.Inject

class PokemonRepository@Inject constructor(
    private val newsApi: PokemonApi,
    private val articleDao: PokemonDao
) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<PokemonListResponse> {
        return newsApi.getBreakingNews(countryCode, pageNumber)
    }

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<PokemonListResponse> {
        return newsApi.searchForNews(searchQuery, pageNumber)
    }

    fun getAllArticles() = articleDao.getArticles()

    suspend fun insertArticle(pokemon: Pokemon) = articleDao.insert(pokemon)

    suspend fun deleteArticle(pokemon: Pokemon) = articleDao.delete(pokemon)
}