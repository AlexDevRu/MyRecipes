package com.example.learning_android_myrecipes_kulakov.ui.add_recipe

import android.content.Context
import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.example.domain.models.Category
import com.example.domain.models.Recipe
import com.example.domain.use_cases.GetRecipeByIdUseCase
import com.example.domain.use_cases.InsertRecipeUseCase
import com.example.domain.use_cases.UpdateRecipeUseCase
import com.example.learning_android_myrecipes_kulakov.R
import com.example.learning_android_myrecipes_kulakov.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor(
    @ApplicationContext context: Context,
    savedStateHandle: SavedStateHandle,
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val updateRecipeUseCase: UpdateRecipeUseCase,
    private val insertRecipeUseCase: InsertRecipeUseCase
) : ViewModel() {

    val categories: Array<out String> = context.resources.getStringArray(R.array.categories)

    val calendar: Calendar = Calendar.getInstance().apply {
        timeInMillis = 0L
    }

    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri : LiveData<Uri> = _imageUri

    val name = ObservableField<String>()
    val category = ObservableField<String>()
    val description = ObservableField<String>()
    val ingredients = ObservableField<List<String>>()

    private val _time = MutableLiveData(0L)
    val timeAsString = _time.map {
        Utils.formatDate(it)
    }

    private val editMode = savedStateHandle.get<Boolean>(AddRecipeFragment.EDIT) == true
    private val id = savedStateHandle.get<Long>(AddRecipeFragment.ID)

    init {
        if (id != null && id >= 0)
            viewModelScope.launch {
                val recipe = getRecipeByIdUseCase(id)
                if (recipe != null) {
                    _imageUri.value = Uri.parse(recipe.photo)
                    name.set(recipe.name)
                    category.set(categories[recipe.category.ordinal])
                    description.set(recipe.description)
                    ingredients.set(recipe.ingredients)
                    _time.value = recipe.time
                }
            }
    }

    fun setTime(hours: Int, minutes: Int) {
        calendar.set(Calendar.HOUR, hours)
        calendar.set(Calendar.MINUTE, minutes)
        _time.value = calendar.timeInMillis
    }

    fun saveImage(uri: Uri) {
        _imageUri.value = uri
    }

    fun addIngredient(ingredient: String) {
        ingredients.set(ingredients.get().orEmpty() + ingredient)
    }

    fun removeIngredient(ingredient: String) {
        ingredients.set(ingredients.get().orEmpty() - ingredient)
    }

    fun save() {
        viewModelScope.launch {
            val recipe = Recipe(
                id = id ?: 0,
                name = name.get().orEmpty(),
                category = Category.values()[categories.indexOf(category.get())],
                ingredients = ingredients.get().orEmpty(),
                description = description.get().orEmpty(),
                photo = imageUri.value?.toString(),
                time = _time.value!!,
            )
            if (editMode)
                updateRecipeUseCase(recipe)
            else
                insertRecipeUseCase(recipe)
            _finish.emit(Unit)
        }
    }
}