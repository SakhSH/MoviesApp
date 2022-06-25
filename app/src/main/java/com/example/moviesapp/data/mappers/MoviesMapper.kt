package com.example.moviesapp.data.mappers

import com.example.moviesapp.data.models.MoviesDto
import com.example.moviesapp.data.models.MultimediaDto
import com.example.moviesapp.domain.models.Movies
import com.example.moviesapp.domain.models.Multimedia
import javax.inject.Inject

class MoviesMapper @Inject constructor() {

    fun mapAllMoviesDto(dtoItems: List<MoviesDto>) = dtoItems.map { mapMoviesDto(it) }

    private fun mapMoviesDto(dtoItem: MoviesDto) = with(dtoItem) {
        Movies(
            displayTitle = dtoItem.displayTitle,
            summaryShort = dtoItem.summaryShort,
            multimedia = mapMultimediaDto(dtoItem.multimedia)
        )
    }

    private fun mapMultimediaDto(dtoItem: MultimediaDto) = with(dtoItem) {
        Multimedia(
            src = dtoItem.src
        )
    }
}