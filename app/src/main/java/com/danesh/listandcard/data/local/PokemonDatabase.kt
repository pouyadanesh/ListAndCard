package com.danesh.listandcard.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.danesh.listandcard.data.model.Pokemon
import com.danesh.listandcard.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Pokemon::class], version = 1)
abstract class PokemonDatabase: RoomDatabase() {

    abstract fun getPokemonDao(): PokemonDao

    class Callback @Inject constructor(
        private val database: Provider<PokemonDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()

}