package com.example.moviesapp.domain.usecases

import com.example.moviesapp.domain.models.Movies
import com.example.moviesapp.domain.MoviesRepository
import javax.inject.Inject

class GetAllMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    suspend operator fun invoke(page: Int): List<Movies> = moviesRepository.getAllMovies(page)
}