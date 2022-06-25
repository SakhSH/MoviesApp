package com.example.moviesapp.data.api

import com.example.moviesapp.data.models.AllMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {

    @GET("reviews/all.json?")
    suspend fun getAllMovies(
        @Query("offset") offset: Int,
        @Query("order") order: String = ORDER,
        @Query("api-key") apiKey: String = API_KEY
    ): AllMoviesResponse

    companion object {
        private const val ORDER = "by-opening-date"
        private const val API_KEY = "30KJkXoV4QRnRC5Yy2qcOiW2YfBGJAn9"
    }
}