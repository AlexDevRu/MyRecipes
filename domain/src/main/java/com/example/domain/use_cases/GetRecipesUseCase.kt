package com.example.domain.use_cases

import com.example.domain.repositories.Repository
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke() = repository.getRecipes()
}
