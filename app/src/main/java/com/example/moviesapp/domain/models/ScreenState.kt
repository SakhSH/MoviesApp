package com.example.moviesapp.domain.models

sealed class ScreenState {

    object Loading : ScreenState()

    data class Content(
        val movies: List<Movies>?,
        val pageButton: MutableList<PageButton>?
    ) : ScreenState()

    data class Error(val text: String) : ScreenState()
}