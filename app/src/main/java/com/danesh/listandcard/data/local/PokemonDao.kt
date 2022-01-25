package com.danesh.listandcard.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.danesh.listandcard.data.model.Pokemon

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon_table")
    fun getPokemons() : LiveData<MutableList<Pokemon>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pokemon: Pokemon) : Long

    @Update
    suspend fun updatePokemon(pokemon: Pokemon)

    @Delete
    suspend fun delete(pokemon: Pokemon)

}