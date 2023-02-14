package com.example.moviesapp.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.domain.usecases.GetAllMoviesUseCase
import com.example.moviesapp.domain.usecases.SearchMoviesUseCase
import com.example.moviesapp.presentation.models.ListItem
import com.example.moviesapp.presentation.models.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getAllMoviesUseCase: GetAllMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
) : ViewModel() {

    private val listMovies: MutableList<ListItem.MoviesItem> by lazy { mutableListOf() }
    private var countPage = FIRST_OFFSET
    private var isLoading = false

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    private val _isOnScrollListenerRecyclerView = MutableStateFlow(false)
    val isOnScrollListenerRecyclerView: StateFlow<Boolean> = _isOnScrollListenerRecyclerView.asStateFlow()

    init {
        loadAllMovies(countPage)
    }

    fun loadAllMovies() {
        listMovies.clear()
        countPage = FIRST_OFFSET
        loadAllMovies(countPage)
    }

    private fun loadAllMovies(offset: Int) {
        viewModelScope.launch {
            if (offset > FIRST_OFFSET){
                _screenState.value = ScreenState.LoadingNextPage
            }else{
                _screenState.value = ScreenState.Loading
            }
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
                _screenState.value = ScreenState.Content(listMovies)
                _isOnScrollListenerRecyclerView.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _screenState.value =
                    ScreenState.Error("An error has occurred", e.message.toString())
            } finally {
                isLoading = false
            }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _screenState.value = ScreenState.Loading
            try {
                _isOnScrollListenerRecyclerView.value = false
                withContext(Dispatchers.IO) {
                    countPage = FIRST_OFFSET
                    val movies = searchMoviesUseCase(query).map {
                        ListItem.MoviesItem(
                            displayTitle = it.displayTitle,
                            summaryShort = it.summaryShort,
                            multimedia = it.multimedia
                        )
                    }
                    _screenState.value = ScreenState.Content(movies)
                }
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
        if (isLoading) return
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
        const val FIRST_OFFSET = 0
    }
}