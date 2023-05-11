package com.example.learning_android_myrecipes_kulakov.ui.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Recipe
import com.example.domain.models.SortOrder
import com.example.domain.use_cases.DeleteRecipeUseCase
import com.example.domain.use_cases.GetRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    getRecipesUseCase: GetRecipesUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase
): ViewModel() {

    private val _query = MutableStateFlow("")
    private val _sort = MutableStateFlow(SortOrder.DESC)

    val query: String
        get() = _query.value

    val sort: SortOrder
        get() = _sort.value

    fun setQuery(query: String) {
        viewModelScope.launch { _query.emit(query) }
    }

    fun setSort(sortOrder: SortOrder) {
        viewModelScope.launch { _sort.emit(sortOrder) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val recipes = combine(_query, _sort) { query, sort ->
        query to sort
    }.flatMapLatest { (query, sort) ->
        getRecipesUseCase().map {
            val filteredList = it.filter { it.name.lowercase().contains(query.lowercase().trim()) }
            when (sort) {
                SortOrder.A_Z -> filteredList.sortedBy { it.name }
                SortOrder.Z_A -> filteredList.sortedByDescending { it.name }
                SortOrder.ASC -> filteredList.sortedBy { it.time }
                else -> filteredList.sortedByDescending { it.time }
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            deleteRecipeUseCase(recipe)
        }
    }

}