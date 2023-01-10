package com.example.moviesapp.domain.usecases

import com.example.moviesapp.domain.MoviesRepository
import com.example.moviesapp.domain.models.Movies
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    suspend operator fun invoke(query: String): List<Movies> = moviesRepository.searchMovies(query)
}