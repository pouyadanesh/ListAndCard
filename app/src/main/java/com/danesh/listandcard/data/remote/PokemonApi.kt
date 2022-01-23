package com.danesh.listandcard.data.remote

import com.danesh.listandcard.data.model.PokemonListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApi {

    @GET("v1/cards")
    suspend fun getBreakingNews(
        @Query("hp") countryCode: String = "gte99",
        @Query("page") pageNumber: Int = 1
    ): Response<PokemonListResponse>

}