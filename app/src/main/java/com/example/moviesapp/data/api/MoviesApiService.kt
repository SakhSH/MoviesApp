package com.example.moviesapp.data.api

import com.example.moviesapp.BuildConfig
import com.example.moviesapp.data.models.AllMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {

    @GET("reviews/all.json?")
    suspend fun getAllMovies(
        @Query("offset") offset: Int,
        @Query("order") order: String = ORDER,
        @Query("api-key") apiKey: String = BuildConfig.API_KEY
    ): AllMoviesResponse

    @GET("reviews/search.json")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("order") order: String = ORDER,
        @Query("api-key") apiKey: String = BuildConfig.API_KEY
    ): AllMoviesResponse

    companion object {
        private const val ORDER = "by-opening-date"
    }
}