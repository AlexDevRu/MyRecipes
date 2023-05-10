package com.example.learning_android_myrecipes_kulakov.ui.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.use_cases.GetRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    getRecipesUseCase: GetRecipesUseCase
): ViewModel() {

    val recipes = getRecipesUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

}