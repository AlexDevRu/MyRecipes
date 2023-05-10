package com.example.domain.repositories

import com.example.domain.models.Recipe
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getRecipes() : Flow<List<Recipe>>
    suspend fun insertRecipe(recipe: Recipe)
}