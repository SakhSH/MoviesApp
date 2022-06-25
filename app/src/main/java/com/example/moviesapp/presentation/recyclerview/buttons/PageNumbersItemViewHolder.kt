package com.example.moviesapp.presentation.recyclerview.buttons

import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.databinding.ItemPageButtonBinding
import com.example.moviesapp.domain.models.PageButton

class PageNumbersItemViewHolder(private val binding: ItemPageButtonBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        pageButton: PageButton,
        changeStateButton: (PageButton) -> Unit,
    ) {
        binding.pageButton.text = pageButton.pageNumber.toString()
        binding.pageButton.isEnabled = pageButton.isActive
        binding.pageButton.setOnClickListener {
            changeStateButton.invoke(pageButton)
        }
    }
}