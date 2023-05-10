package com.example.learning_android_myrecipes_kulakov.ui.binding_adapters

import android.net.Uri
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.size
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.learning_android_myrecipes_kulakov.R
import com.example.learning_android_myrecipes_kulakov.Utils
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


@BindingAdapter("app:uri")
fun loadImage(view: ImageView, uri: Uri?) {
    Glide.with(view)
        .load(uri)
        .error(R.drawable.image_black_24dp)
        .into(view)
}

@BindingAdapter("app:uri")
fun loadImage(view: ImageView, uri: String?) {
    Glide.with(view)
        .load(uri)
        .error(R.drawable.image_black_24dp)
        .into(view)
}

@BindingAdapter("app:date")
fun setDate(textView: TextView, date: Long) {
    textView.text = Utils.formatDate(date)
}

@BindingAdapter("app:dropdownValues")
fun setDropdownValues(textView: AutoCompleteTextView, dropdownValues: Array<out String>) {
    val adapter = ArrayAdapter(textView.context, android.R.layout.simple_list_item_1, dropdownValues)
    textView.setAdapter(adapter)
}

@BindingAdapter("app:chipValues")
fun setChipValues(chipGroup: ChipGroup, chipValues: List<String>?) {
    if (chipGroup.childCount > 1)
        chipGroup.removeViews(0, chipGroup.size - 1)
    chipValues?.reversed()?.forEach {
        val chip = Chip(chipGroup.context)
        chip.text = it
        chip.isCheckable = false
        chipGroup.addView(chip, 0)
    }
}