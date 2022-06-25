package com.example.moviesapp.presentation.recyclerview.movies

import androidx.recyclerview.widget.DiffUtil
import com.example.moviesapp.domain.models.Movies
import javax.inject.Inject

class MoviesItemDiffCallback @Inject constructor() : DiffUtil.ItemCallback<Movies>() {
    override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
        return (oldItem == newItem)
    }
}