package com.example.moviesapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.moviesapp.databinding.FragmentMoviesBinding
import com.example.moviesapp.domain.models.ScreenState
import com.example.moviesapp.presentation.recyclerview.buttons.PageNumbersAdapter
import com.example.moviesapp.presentation.recyclerview.movies.MoviesListAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val viewModel by viewModels<MoviesViewModel>()

    @Inject
    lateinit var moviesAdapter: MoviesListAdapter

    @Inject
    lateinit var pageNumbersAdapter: PageNumbersAdapter

    private var _binding: FragmentMoviesBinding? = null
    private val binding: FragmentMoviesBinding
        get() = _binding ?: throw RuntimeException("FragmentMoviesBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.screenState.observe(viewLifecycleOwner) {
            when (it) {
                is ScreenState.Content -> {
                    showLoading(false)
                    it.movies?.let { moviesAdapter.submitList(it) }
                    it.pageButton?.let { pageNumbersAdapter.submitList(it) }
                }
                is ScreenState.Error -> showError(it.text)
                ScreenState.Loading -> showLoading(true)
            }
        }
    }



    private fun showLoading(isLoading: Boolean) {
        binding.rvPageButtonsList.isVisible = !isLoading
        binding.rvMoviesList.isVisible = !isLoading
        binding.loader.isVisible = isLoading
    }

    private fun showError(message: String) {
        binding.rvMoviesList.isVisible = false
        binding.loader.isVisible = false
    }

    private fun setupRecyclerView() {
        binding.rvMoviesList.adapter = moviesAdapter
        binding.rvPageButtonsList.adapter = pageNumbersAdapter
        pageNumbersAdapter.changeStateButton = {
            viewModel.changeStatePageNumbersButton(it)
        }
    }
}