package com.danesh.listandcard.ui.pokemonlist

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danesh.listandcard.data.model.PokemonListResponse
import com.danesh.listandcard.repository.PokemonRepository
import com.danesh.listandcard.util.Constants.hasInternetConnection
import com.danesh.listandcard.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val searchPokemons: MutableLiveData<Resource<PokemonListResponse>> = MutableLiveData()
    var searchPokemonsResponse: PokemonListResponse? = null
    var searchPokemonsPage = 1

    fun searchPokemons(searchQuery: String) = viewModelScope.launch {
        safeSearchNewCall(searchQuery, searchPokemonsPage)
    }

    private suspend fun safeSearchNewCall(searchQuery: String, searchPokemonsPage: Int) {
        searchPokemons.postValue(Resource.Loading())
        try{
            if(hasInternetConnection(context)){
                val response = repository.getCards("gte"+searchQuery, searchPokemonsPage)
                searchPokemons.postValue(handleSearchPokemonsResponse(response))
            }
            else
                searchPokemons.postValue(Resource.Error("No Internet Connection"))
        }
        catch (ex: Exception){
            when(ex){
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

}