package com.example.learning_android_myrecipes_kulakov

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun formatDate(date: Long) : String {
        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return simpleDateFormat.format(date)
    }
}