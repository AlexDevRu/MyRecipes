package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.entities.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {
    @Query("select * from recipes")
    fun getRecipes() : Flow<List<RecipeEntity>>

    @Insert
    suspend fun insertRecipe(recipeEntity: RecipeEntity)
}