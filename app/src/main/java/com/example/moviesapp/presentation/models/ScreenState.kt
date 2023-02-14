package com.example.moviesapp.presentation.models

sealed class ScreenState {

    object Loading : ScreenState()

    object LoadingNextPage : ScreenState()

    data class Content(val content: List<ListItem.MoviesItem>) : ScreenState()

    data class Error(val text: String, val exception: String) : ScreenState()
}
