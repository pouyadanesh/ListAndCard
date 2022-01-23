package com.danesh.listandcard.data.local

import androidx.room.*
import com.danesh.listandcard.data.model.Pokemon
import io.reactivex.Single

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon_table")
    fun getArticles() : Single<List<Pokemon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: Pokemon) : Long

    @Delete
    suspend fun delete(pokemon: Pokemon)

}