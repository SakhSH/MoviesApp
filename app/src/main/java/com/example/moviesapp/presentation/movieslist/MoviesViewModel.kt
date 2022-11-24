package com.example.moviesapp.presentation.movieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.domain.usecases.GetAllMoviesUseCase
import com.example.moviesapp.presentation.models.ListItem
import com.example.moviesapp.presentation.models.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getAllMoviesUseCase: GetAllMoviesUseCase
) : ViewModel() {

    private val listMovies: MutableList<ListItem.MoviesItem> by lazy { mutableListOf() }
    private var countPage = FIRST_OFFSET
    private var isLoading = false

    private val _screenState = MutableLiveData<ScreenState>()
    val screenState: LiveData<ScreenState> = _screenState

    private val _movies = MutableLiveData<List<ListItem.MoviesItem>>()
    val movies: LiveData<List<ListItem.MoviesItem>> = _movies

    init {
        loadAllMovies(countPage)
    }

    private fun loadAllMovies(offset: Int) {
        viewModelScope.launch {
            _screenState.value = ScreenState.Loading
            try {
                withContext(Dispatchers.IO) {
                    val movies = getAllMoviesUseCase(offset).map {
                        ListItem.MoviesItem(
                            displayTitle = it.displayTitle,
                            summaryShort = it.summaryShort,
                            multimedia = it.multimedia
                        )
                    }
                    if (movies.isNotEmpty()) {
                        listMovies.addAll(movies)
                    }
                }
                _movies.postValue(listMovies)
                _screenState.value = ScreenState.Content
            } catch (e: Exception) {
                e.printStackTrace()
                _screenState.value =
                    ScreenState.Error("An error has occurred", e.message.toString())
            } finally {
                isLoading = false
            }
        }
    }

    fun loadingNextPage() {
        if (isLoading) {
            return
        }
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    isLoading = true
                    countPage++
                    loadAllMovies(countPage * NUMBER_ITEMS_PER_PAGE)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val NUMBER_ITEMS_PER_PAGE = 20
        private const val FIRST_OFFSET = 0
    }
}