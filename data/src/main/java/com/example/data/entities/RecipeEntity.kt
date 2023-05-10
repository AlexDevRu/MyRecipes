package com.example.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val category: String,
    val ingredients: String,
    val description: String,
    val photo: String?,
    val time: Long
)