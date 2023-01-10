package com.example.moviesapp.presentation.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.moviesapp.R
import com.example.moviesapp.databinding.ActivityRootBinding
import com.example.moviesapp.presentation.movies.MoviesFragment
import com.example.moviesapp.presentation.critics.CriticsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_movies_list -> {
                    replaceFragment(MoviesFragment())
                    true
                }
                R.id.navigation_critics -> {
                    replaceFragment(CriticsFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_movies, fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.fragments.lastOrNull { it.isVisible }
        (fragment as? BackPressedListener)?.onBackPressed()
    }
}