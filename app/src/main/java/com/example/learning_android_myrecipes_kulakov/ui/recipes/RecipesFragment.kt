package com.example.learning_android_myrecipes_kulakov.ui.recipes

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.domain.models.Recipe
import com.example.learning_android_myrecipes_kulakov.R
import com.example.learning_android_myrecipes_kulakov.databinding.FragmentRecipesBinding
import com.example.learning_android_myrecipes_kulakov.ui.adapters.RecipesAdapter
import com.example.learning_android_myrecipes_kulakov.ui.add_recipe.AddRecipeFragment
import com.example.learning_android_myrecipes_kulakov.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(), RecipesAdapter.Listener, MenuProvider {

    private lateinit var binding: FragmentRecipesBinding

    private val viewModel by viewModels<RecipesViewModel>()

    private val recipesAdapter = RecipesAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.adapter = recipesAdapter
        (requireActivity() as MenuHost).addMenuProvider(
            this,
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipes.collectLatest {
                    recipesAdapter.submitList(it)
                }
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchView = menu.findItem(R.id.search)?.actionView as? SearchView
        //searchView?.setQuery(viewModel.query, false)
        //searchView?.setOnQueryTextListener(this)
    }

    private fun openAddRecipeFragment(id: Long = -1, edit: Boolean = false) {
        val fragment = AddRecipeFragment.createInstance(id, edit)
        (requireActivity() as MainActivity).setFragment(fragment)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.add -> {
                openAddRecipeFragment()
                true
            }
            R.id.sort -> {
                /*AlertDialog.Builder(requireContext())
                    .setTitle(R.string.sort)
                    .setSingleChoiceItems(R.array.sort, viewModel.sort.ordinal, this)
                    .show()*/
                true
            }
            else -> false
        }
    }

    override fun onItemView(recipe: Recipe) {
        openAddRecipeFragment(recipe.id)
    }

    override fun onItemEdit(recipe: Recipe) {
        openAddRecipeFragment(recipe.id, true)
    }

    override fun onItemDelete(recipe: Recipe) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete)
            .setMessage(R.string.delete_confirmation)
            .setPositiveButton(android.R.string.ok) { _, _ -> viewModel.deleteRecipe(recipe) }
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }

}