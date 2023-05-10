package com.example.domain.use_cases

import com.example.domain.repositories.Repository
import javax.inject.Inject

class GetRecipeByIdUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Long) = repository.getRecipeById(id)
}