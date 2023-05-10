package com.example.data.repositories

import com.example.data.dao.RecipesDao
import com.example.data.mappers.toDomainModel
import com.example.data.mappers.toEntity
import com.example.domain.models.Recipe
import com.example.domain.repositories.Repository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val recipesDao: RecipesDao,
    private val gson: Gson
): Repository {

    override fun getRecipes(): Flow<List<Recipe>> {
        return recipesDao.getRecipes().map { recipes ->
            recipes.map { it.toDomainModel(gson) }
        }
    }

    override suspend fun insertRecipe(recipe: Recipe) = withContext(Dispatchers.IO) {
        recipesDao.insertRecipe(recipe.toEntity(gson))
    }

}