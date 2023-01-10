package com.example.moviesapp.data.api

import com.example.moviesapp.data.mappers.MoviesMapper
import com.example.moviesapp.domain.MoviesRepository
import com.example.moviesapp.domain.models.Movies
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val apiService: MoviesApiService,
    private val mapper: MoviesMapper
) : MoviesRepository {

    override suspend fun getAllMovies(page: Int): List<Movies> {
        val movies = apiService.getAllMovies(page).results
        return try {
            mapper.mapAllMoviesDto(movies)
        } catch (e: Exception) {
            listOf()
        }
    }

    override suspend fun searchMovies(query: String): List<Movies> {
        val movies = apiService.searchMovies(query).results
        return try {
            mapper.mapAllMoviesDto(movies)
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }
}