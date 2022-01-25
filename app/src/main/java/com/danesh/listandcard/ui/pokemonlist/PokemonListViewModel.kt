package com.danesh.listandcard.ui.pokemonlist

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danesh.listandcard.data.model.Pokemon
import com.danesh.listandcard.data.model.PokemonListResponse
import com.danesh.listandcard.repository.PokemonRepository
import com.danesh.listandcard.ui.pokemon.PokemonViewModel
import com.danesh.listandcard.util.Constants.hasInternetConnection
import com.danesh.listandcard.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    fun getAllPokemons() = repository.getAllPokemons()
    val searchPokemons: MutableLiveData<Resource<PokemonListResponse>> = MutableLiveData()
    var searchPokemonsResponse: PokemonListResponse? = null
    var searchPokemonsPage = 1

    fun searchPokemons() = viewModelScope.launch {
        safeSearchNewCall(searchPokemonsPage)
    }



    private suspend fun safeSearchNewCall(searchPokemonsPage: Int) {
        searchPokemons.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(context)) {
                val response = repository.getCards(searchPokemonsPage)
                searchPokemons.postValue(handleSearchPokemonsResponse(response))
            } else {
                searchPokemons.postValue(Resource.Error("Connection Error"))
            }
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> searchPokemons.postValue(Resource.Error("Network Failure"))
                else -> searchPokemons.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleSearchPokemonsResponse(response: Response<PokemonListResponse>): Resource<PokemonListResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchPokemonsPage++
                if (searchPokemonsResponse == null)
                    searchPokemonsResponse = resultResponse
                else {
                    val oldPokemons = searchPokemonsResponse?.cards
                    val newPokemons = resultResponse.cards
                    oldPokemons?.addAll(newPokemons)
                }
                return Resource.Success(searchPokemonsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun insertPokemons(cards: List<Pokemon>) = viewModelScope.launch {
        for(c in cards) {
            val n = repository.insertPokemon(c)
            Log.e("MYTAG","this is return insert value $n")
        }
    }

    fun updatePokemon(pokemon: Pokemon) = viewModelScope.launch {
        repository.updatePokemon(pokemon)
    }

}