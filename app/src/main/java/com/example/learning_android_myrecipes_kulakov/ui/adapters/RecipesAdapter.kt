package com.example.learning_android_myrecipes_kulakov.ui.adapters

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Recipe
import com.example.learning_android_myrecipes_kulakov.R
import com.example.learning_android_myrecipes_kulakov.databinding.ListItemRecipeBinding

class RecipesAdapter(
    private val listener: Listener
): ListAdapter<Recipe, RecipesAdapter.ContactViewHolder>(DIFF_UTIL) {

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface Listener {
        fun onItemView(recipe: Recipe)
        fun onItemEdit(recipe: Recipe)
        fun onItemDelete(recipe: Recipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ListItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ContactViewHolder(
        private val binding: ListItemRecipeBinding
    ) : RecyclerView.ViewHolder(binding.root), PopupMenu.OnMenuItemClickListener, View.OnClickListener {

        private val popupMenu = PopupMenu(binding.root.context, binding.btnMore)

        init {
            popupMenu.inflate(R.menu.popup_recipe)
            popupMenu.setOnMenuItemClickListener(this)
            binding.btnMore.setOnClickListener(this)
            binding.root.setOnClickListener(this)
        }

        fun bind(recipe: Recipe) {
            binding.recipe = recipe
        }

        override fun onClick(view: View?) {
            when (view) {
                binding.btnMore -> popupMenu.show()
                binding.root -> binding.recipe?.let { listener.onItemView(it) }
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.view -> {
                    binding.recipe?.let { listener.onItemView(it) }
                    true
                }
                R.id.edit -> {
                    binding.recipe?.let { listener.onItemEdit(it) }
                    true
                }
                R.id.delete -> {
                    binding.recipe?.let { listener.onItemDelete(it) }
                    true
                }
                else -> false
            }
        }

    }
}