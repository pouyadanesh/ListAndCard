package com.danesh.listandcard.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.danesh.listandcard.data.model.Pokemon

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon_table")
    fun getArticles() : LiveData<List<Pokemon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: Pokemon) : Long

    @Delete
    suspend fun delete(pokemon: Pokemon)

}