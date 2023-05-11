package com.example.learning_android_myrecipes_kulakov.ui.binding_adapters

import android.net.Uri
import android.view.View
import android.widget.*
import androidx.core.view.size
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.bumptech.glide.Glide
import com.example.learning_android_myrecipes_kulakov.R
import com.example.learning_android_myrecipes_kulakov.Utils
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


@BindingAdapter("uri")
fun loadImage(view: ImageView, uri: Uri?) {
    Glide.with(view)
        .load(uri)
        .error(R.drawable.image_black_24dp)
        .into(view)
}

@BindingAdapter("uri")
fun loadImage(view: ImageView, uri: String?) {
    Glide.with(view)
        .load(uri)
        .error(R.drawable.image_black_24dp)
        .into(view)
}

@BindingAdapter("date")
fun setDate(textView: TextView, date: Long) {
    textView.text = Utils.formatDate(date)
}

@BindingAdapter("dropdownValues")
fun setDropdownValues(textView: AutoCompleteTextView, dropdownValues: Array<out String>) {
    val adapter = ArrayAdapter(textView.context, android.R.layout.simple_list_item_1, dropdownValues)
    textView.setAdapter(adapter)
}

@BindingAdapter("selectedValue")
fun setSelectedValue(textView: AutoCompleteTextView, selectedValue: String?) {
    textView.setText(selectedValue, false)
}

@InverseBindingAdapter(attribute = "selectedValue")
fun getSelectedValue(textView: AutoCompleteTextView) : String? {
    return textView.text?.toString()
}

@BindingAdapter("selectedValueAttrChanged")
fun setSelectedValueListener(textView: AutoCompleteTextView, attrChange: InverseBindingListener) {
    textView.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ -> attrChange.onChange() }
}

@BindingAdapter("chipValues", "onLongClickChipListener")
fun setChipValues(chipGroup: ChipGroup, chipValues: List<String>?, onLongClickChipListener: View.OnLongClickListener) {
    if (chipGroup.childCount > 1)
        chipGroup.removeViews(0, chipGroup.size - 1)
    chipValues?.reversed()?.forEach {
        val chip = Chip(chipGroup.context)
        chip.text = it
        chip.isCheckable = false
        chip.setOnLongClickListener(onLongClickChipListener)
        chipGroup.addView(chip, 0)
    }
}