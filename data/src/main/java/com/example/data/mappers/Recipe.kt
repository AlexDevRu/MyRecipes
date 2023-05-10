package com.example.data.mappers

import com.example.data.entities.RecipeEntity
import com.example.domain.models.Category
import com.example.domain.models.Recipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun RecipeEntity.toDomainModel(gson: Gson) = Recipe(
    id = id,
    name = name,
    category = Category.valueOf(category),
    ingredients = gson.fromJson(ingredients, object : TypeToken<List<String>>() {}),
    description = description,
    photo = photo,
    time = time
)

fun Recipe.toEntity(gson: Gson) = RecipeEntity(
    id = id,
    name = name,
    category = category.name,
    ingredients = gson.toJson(ingredients),
    description = description,
    photo = photo,
    time = time
)