package com.example.domain.models

data class Recipe(
    val id: Long,
    val name: String,
    val category: Category,
    val ingredients: List<String>,
    val description: String,
    val photo: String?,
    val time: Long
)
