package com.example.moviesapp.presentation.critics

import androidx.lifecycle.ViewModel
import com.example.moviesapp.domain.usecases.GetAllMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CriticsViewModel @Inject constructor(
    private val getAllMoviesUseCase: GetAllMoviesUseCase
) : ViewModel() {


}