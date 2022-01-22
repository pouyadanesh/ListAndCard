package com.danesh.listandcard.data.remote

import com.danesh.listandcard.data.model.PokemonListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode: String = "tr",
        @Query("page") pageNumber: Int = 1
    ): Response<PokemonListResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int = 1
    ): Response<PokemonListResponse>

}