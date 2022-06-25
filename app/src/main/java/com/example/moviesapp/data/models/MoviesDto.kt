package com.example.moviesapp.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MoviesDto(

    @SerializedName("display_title")
    @Expose
    val displayTitle: String?,
    @SerializedName("summary_short")
    @Expose
    val summaryShort: String?,
    @SerializedName("multimedia")
    @Expose
    val multimedia: MultimediaDto,
)
