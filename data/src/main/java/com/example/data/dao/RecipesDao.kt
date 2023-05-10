package com.example.data.dao

import androidx.room.*
import com.example.data.entities.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {
    @Query("select * from recipes")
    fun getRecipes() : Flow<List<RecipeEntity>>

    @Query("select * from recipes where id=:id")
    suspend fun getRecipeById(id: Long) : RecipeEntity?

    @Insert
    suspend fun insertRecipe(recipeEntity: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipeEntity: RecipeEntity)

    @Update
    suspend fun updateRecipe(recipeEntity: RecipeEntity)
}