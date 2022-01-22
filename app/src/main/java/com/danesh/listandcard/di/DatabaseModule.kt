package com.danesh.listandcard.di

import android.app.Application
import androidx.room.Room
import com.danesh.listandcard.data.local.PokemonDao
import com.danesh.listandcard.data.local.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application, callback: PokemonDatabase.Callback): PokemonDatabase{
        return Room.databaseBuilder(application, PokemonDatabase::class.java, "pokemon_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideArticleDao(db: PokemonDatabase): PokemonDao{
        return db.getPokemonDao()
    }
}