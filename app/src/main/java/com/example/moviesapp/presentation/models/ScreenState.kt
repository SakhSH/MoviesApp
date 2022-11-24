package com.example.moviesapp.presentation.models

sealed class ScreenState {

    object Loading: ScreenState()

    object Content: ScreenState()

    data class Error(val text: String, val exception: String) : ScreenState()
}
