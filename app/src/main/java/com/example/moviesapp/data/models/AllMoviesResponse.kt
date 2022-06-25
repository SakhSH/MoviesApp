package com.example.moviesapp.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AllMoviesResponse(
    @SerializedName("results")
    @Expose
    val results: List<MoviesDto>
)