package com.example.domain.repositories

import com.example.domain.models.Recipe
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getRecipes() : Flow<List<Recipe>>
    suspend fun insertRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipe: Recipe)
    suspend fun getRecipeById(id: Long) : Recipe?
    suspend fun updateRecipe(recipe: Recipe)
}