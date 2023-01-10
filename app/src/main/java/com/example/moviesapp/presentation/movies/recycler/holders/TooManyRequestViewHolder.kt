package com.example.moviesapp.presentation.movies.recycler.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.databinding.ItemMoviesTooManyRequestsBinding

class TooManyRequestViewHolder(private val binding: ItemMoviesTooManyRequestsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        tryAgainLoadAllMovies: (() -> Unit)?,
    ) {
        binding.buttonTryAgain.setOnClickListener { tryAgainLoadAllMovies?.invoke() }
    }
}