package com.example.moviesapp.presentation.recyclerview.movies

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.databinding.ItemMoviesBinding
import com.example.moviesapp.domain.models.Movies

class MoviesItemViewHolder(private val binding: ItemMoviesBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        movie: Movies,
    ) {
        Glide.with(itemView.context)
            .load(movie.multimedia.src)
            .into(binding.ivLogoMovie)
        binding.tvTitleText.text = movie.displayTitle
        binding.tvDescriptionText.text = movie.summaryShort
    }
}