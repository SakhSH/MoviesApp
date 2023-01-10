package com.example.moviesapp.presentation.movies.recycler.holders

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.databinding.ItemMoviesLoadingBinding

class LoaderViewHolder(private val binding: ItemMoviesLoadingBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.loader.isVisible = true
    }
}