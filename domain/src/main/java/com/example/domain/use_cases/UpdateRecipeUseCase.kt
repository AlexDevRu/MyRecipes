package com.example.domain.use_cases

import com.example.domain.models.Recipe
import com.example.domain.repositories.Repository
import javax.inject.Inject

class UpdateRecipeUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(recipe: Recipe) = repository.updateRecipe(recipe)
}