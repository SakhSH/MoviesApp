package com.example.moviesapp.presentation.recyclerview.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.moviesapp.databinding.ItemMoviesBinding
import com.example.moviesapp.domain.models.Movies
import javax.inject.Inject

class MoviesListAdapter @Inject constructor(
    callback: MoviesItemDiffCallback,
) :
    ListAdapter<Movies, MoviesItemViewHolder>(callback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MoviesItemViewHolder(ItemMoviesBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(viewHolder: MoviesItemViewHolder, position: Int) {
        val memeItem = getItem(position)
        viewHolder.bind(memeItem)
    }
}