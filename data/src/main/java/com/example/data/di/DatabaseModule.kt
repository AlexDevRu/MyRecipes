package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.dao.RecipesDatabase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideRecipesDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        RecipesDatabase::class.java,
        "recipes_db"
    ).build()

    @Singleton
    @Provides
    fun provideRecipesDao(db: RecipesDatabase) = db.getRecipesDao()

    @Singleton
    @Provides
    fun provideGson() = Gson()
}