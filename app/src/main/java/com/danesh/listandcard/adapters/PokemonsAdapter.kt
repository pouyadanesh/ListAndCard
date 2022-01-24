package com.danesh.listandcard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danesh.listandcard.R
import com.danesh.listandcard.data.model.Pokemon
import com.danesh.listandcard.databinding.ItemPokemonPreviewBinding

class PokemonsAdapter(private val listener: OnItemClickListener): ListAdapter<Pokemon, PokemonsAdapter.PokemonViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonPreviewBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class PokemonViewHolder(private val binding: ItemPokemonPreviewBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val pokemon = getItem(position)
                        listener.onItemClick(pokemon)
                    }
                }
            }
        }

        fun bind(pokemon: Pokemon){
            binding.apply {
                Glide.with(itemView)
                    .load(pokemon.imageUrl)
                    .into(ivArticleImage)
                tvTitle.text = pokemon.name
                tvSource.text = root.resources.getString(R.string.txtArtist,pokemon.artist)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(pokemon: Pokemon)
    }


    class DiffCallback : DiffUtil.ItemCallback<Pokemon>(){
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem == newItem
        }

    }
}