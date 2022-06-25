package com.example.moviesapp.presentation.recyclerview.buttons

import androidx.recyclerview.widget.DiffUtil
import com.example.moviesapp.domain.models.PageButton
import javax.inject.Inject

class PageNumbersItemDiffCallback @Inject constructor() : DiffUtil.ItemCallback<PageButton>() {
    override fun areItemsTheSame(oldItem: PageButton, newItem: PageButton): Boolean {
        return oldItem.pageNumber == newItem.pageNumber
    }

    override fun areContentsTheSame(oldItem: PageButton, newItem: PageButton): Boolean {
        return (oldItem == newItem)
    }
}