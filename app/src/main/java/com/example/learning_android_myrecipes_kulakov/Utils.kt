package com.example.learning_android_myrecipes_kulakov

import android.text.InputType
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun formatDate(date: Long) : String {
        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    fun EditText.disable() {
        inputType = InputType.TYPE_NULL
        isFocusable = false
    }
}