package com.example.moviesapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.domain.models.Movies
import com.example.moviesapp.domain.models.PageButton
import com.example.moviesapp.domain.models.ScreenState
import com.example.moviesapp.domain.usecases.GetAllMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getAllMoviesUseCase: GetAllMoviesUseCase
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenState>()
    val screenState: LiveData<ScreenState> = _screenState

    private val _movies = MutableLiveData<List<Movies>>()
    private val _pageButtons = MutableLiveData<MutableList<PageButton>>()

    init {
        loadAllMovies(FIRST_OFFSET)
        _pageButtons.value = createPageButtons(FIRST_OFFSET)
    }

    private fun loadAllMovies(offset: Int) {
        viewModelScope.launch {
            _screenState.value = ScreenState.Loading
            try {
                withContext(Dispatchers.IO) {
                    val movies = getAllMoviesUseCase(offset)
                    _movies.postValue(movies)
                }
                _screenState.value = ScreenState.Content(_movies.value, _pageButtons.value)
            } catch (e: Exception) {
                e.printStackTrace()
                _screenState.value = ScreenState.Error("Ошибка соединения")
            }
        }
    }

    private fun loadingNextPage(pageButton: PageButton) {
        try {
            loadAllMovies((pageButton.pageNumber - 1) * NUMBER_ITEMS_PER_PAGE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createPageButtons(selectedIndex: Int): MutableList<PageButton> {
        val listPageButtons = mutableListOf<PageButton>()

        repeat(19) { index ->
            listPageButtons.add(PageButton(index + 1, index != selectedIndex))
        }
        return listPageButtons
    }

    fun changeStatePageNumbersButton(pageButton: PageButton) {
        loadingNextPage(pageButton)
        _pageButtons.value = createPageButtons(pageButton.pageNumber - 1)
    }

    companion object {
        const val NUMBER_ITEMS_PER_PAGE = 20
        const val FIRST_OFFSET = 0
    }
}