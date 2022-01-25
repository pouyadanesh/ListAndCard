package com.danesh.listandcard.ui.pokemonlist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.Resource
import com.danesh.listandcard.R
import com.danesh.listandcard.adapters.PokemonsAdapter
import com.danesh.listandcard.data.model.Pokemon
import com.danesh.listandcard.databinding.FragmentPokemonListBinding
import com.danesh.listandcard.util.Constants.QUERY_PAGE_SIZE
import com.danesh.listandcard.util.Constants.SEARCH_NEWS_TIME_DELAY
import com.danesh.listandcard.util.Constants.hasInternetConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "PokemonListFragment"

@AndroidEntryPoint
class PokemonListFragment: Fragment(R.layout.fragment_pokemon_list),
    PokemonsAdapter.OnItemClickListener, PokemonsAdapter.OnLikeListener{

    private val viewModel: PokemonListViewModel by viewModels()
    var isLastPage = false
    var isScrolling = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPokemonListBinding.bind(view)
        val pokemonsAdapter = PokemonsAdapter(this,this)

        binding.apply {
            rvPokemonCards.apply {
                adapter = pokemonsAdapter
                setHasFixedSize(true)
                addOnScrollListener(this@PokemonListFragment.scrollListener)
            }
        }

        if(hasInternetConnection(requireContext())){
            viewModel.searchPokemons()
        } else {
            viewModel.getAllPokemons().observe(viewLifecycleOwner) {
                pokemonsAdapter.submitList(it)
            }
        }

        viewModel.searchPokemons.observe(viewLifecycleOwner) {
            when (it) {
                is com.danesh.listandcard.util.Resource.Success -> {
                    it.data?.let { newsResponse ->
                        pokemonsAdapter.submitList(newsResponse.cards.toList())
                        viewModel.insertPokemons(newsResponse.cards.toList())
                    }
                }
                is com.danesh.listandcard.util.Resource.Error -> {
                    it.message?.let { message ->
                        Log.e(TAG, "Error: $message")
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){ //State is scrolling
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val totalVisibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isAtLastItem = firstVisibleItemPosition + totalVisibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = !isLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                viewModel.searchPokemons()
                isScrolling = false
            }
        }
    }

    override fun onItemClick(pokemon: Pokemon) {
        val action = PokemonListFragmentDirections.actionPokemonListFragmentToPokemonFragment(pokemon)
        findNavController().navigate(action)
    }

    override fun onLike(pokemon: Pokemon) {
        pokemon.isLike.let {
            if (it!=null)
                !it
            else
                true
        }
        viewModel.updatePokemon(pokemon)
    }
}