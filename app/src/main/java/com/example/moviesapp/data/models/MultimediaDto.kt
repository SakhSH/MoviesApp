package com.example.moviesapp.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MultimediaDto(
    @SerializedName("src")
    @Expose
    val src: String?
)
