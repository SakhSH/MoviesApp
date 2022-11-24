package com.example.moviesapp.presentation.models

import com.example.moviesapp.domain.models.Multimedia

sealed class ListItem {

    data class MoviesItem(
        val displayTitle: String?,
        val summaryShort: String?,
        val multimedia: Multimedia
    ) : ListItem()

    object Loader : ListItem()

    object TooManyRequest : ListItem()
}
