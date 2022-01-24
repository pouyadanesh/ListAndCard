package com.danesh.listandcard.ui.pokemon

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.danesh.listandcard.R
import com.danesh.listandcard.databinding.FragmentPokemonBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PokemonFragment: Fragment(R.layout.fragment_pokemon) {
    private val viewModel: PokemonViewModel by viewModels()
    private val args by navArgs<PokemonFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPokemonBinding.bind(view)
        binding.apply {
            val pokemon = args.pokemon
            Glide.with(view)
                .load(pokemon.imageUrl)
                .into(ivPokemonImage)
            tvTitle.text = pokemon.name
            tvSource.text = resources.getString(R.string.txtArtist,pokemon.artist)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.pokemonEvent.collect { event ->
                when(event) {
                    is PokemonViewModel.PokemonEvent.ShowPokemonSavedMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}