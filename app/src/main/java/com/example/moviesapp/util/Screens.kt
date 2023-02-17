package com.example.moviesapp.util

import com.example.moviesapp.presentation.critics.CriticsFragment
import com.example.moviesapp.presentation.movies.MoviesFragment
import com.example.moviesapp.presentation.welcome.WelcomeFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

    fun welcome() = FragmentScreen { WelcomeFragment() }

    fun movies() = FragmentScreen { MoviesFragment() }

    fun critics() = FragmentScreen { CriticsFragment() }

}