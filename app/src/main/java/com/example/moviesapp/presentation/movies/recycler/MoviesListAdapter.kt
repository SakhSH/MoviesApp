package com.example.moviesapp.presentation.movies.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.presentation.models.ListItem
import com.example.moviesapp.presentation.movies.recycler.holders.LoaderViewHolder
import com.example.moviesapp.presentation.movies.recycler.holders.MoviesItemViewHolder
import com.example.moviesapp.presentation.movies.recycler.holders.TooManyRequestViewHolder
import com.example.moviesapp.databinding.ItemMoviesBinding
import com.example.moviesapp.databinding.ItemMoviesLoadingBinding
import com.example.moviesapp.databinding.ItemMoviesTooManyRequestsBinding
import javax.inject.Inject

class MoviesListAdapter @Inject constructor(
    callback: MoviesItemDiffCallback,
) :
    ListAdapter<ListItem, RecyclerView.ViewHolder>(callback) {

    lateinit var tryAgainLoadAllMovies: (() -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_ITEM ->
                MoviesItemViewHolder(ItemMoviesBinding.inflate(inflater, parent, false))
            VIEW_TYPE_LOADER ->
                LoaderViewHolder(ItemMoviesLoadingBinding.inflate(inflater, parent, false))
            VIEW_TYPE_TOO_MANY_REQUEST ->
                TooManyRequestViewHolder(
                    ItemMoviesTooManyRequestsBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (val moviesItem = getItem(position)) {
            is ListItem.MoviesItem -> (viewHolder as MoviesItemViewHolder).bind(
                moviesItem
            )
            is ListItem.Loader -> (viewHolder as LoaderViewHolder).bind()
            is ListItem.TooManyRequest -> (viewHolder as TooManyRequestViewHolder).bind(
                tryAgainLoadAllMovies
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            ListItem.Loader -> {
                VIEW_TYPE_LOADER
            }
            ListItem.TooManyRequest -> {
                VIEW_TYPE_TOO_MANY_REQUEST
            }
            else -> {
                VIEW_TYPE_ITEM
            }
        }
    }

    fun createLoader() {
        if (currentList.isEmpty())  return
        if (currentList[currentList.lastIndex] != ListItem.Loader) {
            val listMut: MutableList<ListItem> = currentList.toMutableList()
            listMut.add(ListItem.Loader)
            submitList(listMut)
        } else {
            return
        }
    }

    fun createTooManyRequest() {
        val listMut: MutableList<ListItem> = currentList.toMutableList()
        if (currentList[currentList.lastIndex] == ListItem.Loader) {
            listMut.removeLast()
            if (currentList[currentList.lastIndex - 1] != ListItem.TooManyRequest) {
                listMut.add(ListItem.TooManyRequest)
            }
            submitList(listMut)
        } else {
            return
        }
    }

    companion object {
        const val VIEW_TYPE_ITEM = 1
        const val VIEW_TYPE_LOADER = 2
        const val VIEW_TYPE_TOO_MANY_REQUEST = 3
    }
}