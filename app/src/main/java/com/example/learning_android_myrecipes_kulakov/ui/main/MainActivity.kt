package com.example.learning_android_myrecipes_kulakov.ui.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.example.learning_android_myrecipes_kulakov.R
import com.example.learning_android_myrecipes_kulakov.ui.recipes.RecipesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    private var isLandscapeOrTablet = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isLandscapeOrTablet = findViewById<FragmentContainerView>(R.id.fragmentContainerRight) != null

        val fragmentsSize = supportFragmentManager.fragments.size
        if (fragmentsSize == 0)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerLeft, RecipesFragment())
                .commit()

        supportFragmentManager.addOnBackStackChangedListener(this)
    }

    override fun onBackStackChanged() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        supportActionBar?.setDisplayHomeAsUpEnabled(backStackEntryCount > 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            supportFragmentManager.popBackStack()
            return true
        }
        return false
    }

    fun setFragment(fragment: Fragment) {
        val fragmentContainerId = if (isLandscapeOrTablet) R.id.fragmentContainerRight else R.id.fragmentContainerLeft
        supportFragmentManager.beginTransaction()
            .replace(fragmentContainerId, fragment)
            .addToBackStack(null)
            .commit()
    }
}