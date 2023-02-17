package com.example.moviesapp.presentation.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.util.Screens
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val router: Router,
) : ViewModel() {

    fun navigateToMovies() {
        viewModelScope.launch {
            delay(2000)
            withContext(Dispatchers.Main) {
                router.replaceScreen(Screens.movies())
            }
        }
    }
}