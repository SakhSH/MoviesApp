package com.example.moviesapp.domain

import com.example.moviesapp.domain.models.Movies

interface MoviesRepository {
    suspend fun getAllMovies(page: Int): List<Movies>

    suspend fun searchMovies(query: String) : List<Movies>
}