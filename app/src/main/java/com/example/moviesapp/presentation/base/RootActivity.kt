package com.example.moviesapp.presentation.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.moviesapp.R
import com.example.moviesapp.databinding.ActivityRootBinding
import com.example.moviesapp.util.Screens
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    private var pressedTime: Long = 0

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator = object : AppNavigator(
        activity = this,
        containerId = R.id.container,
        fragmentManager = supportFragmentManager
    ) {
        override fun setupFragmentTransaction(
            screen: FragmentScreen,
            fragmentTransaction: FragmentTransaction,
            currentFragment: Fragment?,
            nextFragment: Fragment
        ) {
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            changeVisibilityBottomNavBar(screen)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupBottomNavigation()
        navigateToWelcomeFragment()
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.fragments.lastOrNull { it.isVisible }
        if (fragment is BackPressedListener) {
            (fragment as? BackPressedListener)?.onBackPressed() ?: router.exit()
        } else {
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                this.finish()
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.on_back_pressed_text),
                    Toast.LENGTH_SHORT
                ).show()
            }
            pressedTime = System.currentTimeMillis()
        }
    }

    private fun navigateToWelcomeFragment() {
        router.navigateTo(Screens.welcome())
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_movies_list -> {
                    replaceFragment(Screens.movies())
                    true
                }
                R.id.navigation_critics -> {
                    replaceFragment(Screens.critics())
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun replaceFragment(screen: FragmentScreen) {
        router.navigateTo(screen)
    }

    private fun changeVisibilityBottomNavBar(screen: FragmentScreen) {
        with(binding.bottomNavigation) {
            isVisible = when (screen.screenKey) {
                Screens.movies().screenKey -> true
                Screens.critics().screenKey -> true
                else -> false
            }
        }
    }
}