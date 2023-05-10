package com.example.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.entities.RecipeEntity

@Database(
    entities = [RecipeEntity::class],
    version = 1
)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun getRecipesDao() : RecipesDao
}