package com.example.moviesapp.presentation.movies.recycler

import androidx.recyclerview.widget.DiffUtil
import com.example.moviesapp.presentation.models.ListItem
import javax.inject.Inject

class MoviesItemDiffCallback @Inject constructor() : DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return if (isLoaderOrTooManyRequest(oldItem, newItem)) {
            false
        } else {
            if (oldItem is ListItem.MoviesItem && newItem is ListItem.MoviesItem) {
                if (oldItem.displayTitle.isNullOrEmpty() || newItem.displayTitle.isNullOrEmpty()) {
                    false
                } else {
                    oldItem.displayTitle == newItem.displayTitle
                }
            } else {
                false
            }
        }
    }

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem == newItem
    }

    private fun isLoaderOrTooManyRequest(
        oldItem: ListItem, newItem: ListItem
    ) = oldItem == ListItem.Loader
            || newItem == ListItem.Loader || oldItem == ListItem.TooManyRequest
            || newItem == ListItem.TooManyRequest
}




