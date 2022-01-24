package com.danesh.listandcard.ui.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danesh.listandcard.data.model.Pokemon
import com.danesh.listandcard.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val repository: PokemonRepository
): ViewModel() {

    private val pokemonEventChannel = Channel<PokemonEvent>()
    val pokemonEvent = pokemonEventChannel.receiveAsFlow()

    fun savePokemon(pokemon: Pokemon) {
        viewModelScope.launch {
            repository.insertPokemon(pokemon)
            pokemonEventChannel.send(PokemonEvent.ShowPokemonSavedMessage("Article Saved."))
        }
    }

    sealed class PokemonEvent{
        data class ShowPokemonSavedMessage(val message: String): PokemonEvent()
    }

}