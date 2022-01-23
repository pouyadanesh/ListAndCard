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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "PokemonListFragment"

@AndroidEntryPoint
class PokemonListFragment: Fragment(R.layout.fragment_pokemon_list),
    PokemonsAdapter.OnItemClickListener{

    private val viewModel: PokemonListViewModel by viewModels()
    var isLastPage = false
    var isScrolling = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPokemonListBinding.bind(view)
        val pokemonsAdapter = PokemonsAdapter(this)

        binding.apply {
            rvPokemonCards.apply {
                adapter = pokemonsAdapter
                setHasFixedSize(true)
                addOnScrollListener(this@PokemonListFragment.scrollListener)
            }
        }

        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().length>1) {
                        viewModel.searchPokemons(editable.toString())
                    }
                }
            }
        }

        viewModel.searchPokemons.observe(viewLifecycleOwner) {
            when (it) {
                is com.danesh.listandcard.util.Resource.Success -> {
                    it.data?.let { newsResponse ->
                        pokemonsAdapter.submitList(newsResponse.cards.toList())
//                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
//                        isLastPage = viewModel.searchPokemonsPage == totalPages
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
                viewModel.searchPokemons(etSearch.text.toString())
                isScrolling = false
            }
        }
    }

    override fun onItemClick(pokemon: Pokemon) {
        val action = PokemonListFragmentDirections.actionPokemonListFragmentToPokemonFragment(pokemon)
        findNavController().navigate(action)
    }
}