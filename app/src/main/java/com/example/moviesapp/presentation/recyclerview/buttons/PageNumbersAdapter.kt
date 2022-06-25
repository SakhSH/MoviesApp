package com.example.moviesapp.presentation.recyclerview.buttons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.moviesapp.databinding.ItemPageButtonBinding
import com.example.moviesapp.domain.models.PageButton
import javax.inject.Inject

class PageNumbersAdapter @Inject constructor(
    callback: PageNumbersItemDiffCallback,
) :
    ListAdapter<PageButton, PageNumbersItemViewHolder>(callback) {
    lateinit var changeStateButton: (PageButton) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageNumbersItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PageNumbersItemViewHolder(ItemPageButtonBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: PageNumbersItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, changeStateButton)
    }
}